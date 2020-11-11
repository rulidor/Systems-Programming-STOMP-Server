package bgu.spl.net.api;

import bgu.spl.net.Frames.*;
import bgu.spl.net.Frames.Error;
import bgu.spl.net.srv.Connections;
import bgu.spl.net.srv.ConnectionsImpl;
import bgu.spl.net.srv.DataStorage;
import bgu.spl.net.srv.User;
import javax.xml.crypto.Data;
import java.util.ArrayList;

public class StompMessagingProtocolImpl<T> implements StompMessagingProtocol<T>{

    private static int messageIDCounter = 1; //unique Id for every MESSAGE frame
    private boolean shouldTerminate;
    private int connectionID;
    private ConnectionsImpl connections;
    private User user;

    @Override
    public void start(int connectionId, ConnectionsImpl connections) {
        this.connectionID = connectionId;
        this.connections = connections;
        shouldTerminate = false;
        //user = DataStorage.getInstance().getUsersByID().get(connectionId);
        this.user= new User();
    }

    @Override
    public void process(T frame) {
        System.out.println("protocol processing...");
        System.out.println("*******protocol got the Frame: \n"+frame.toString());

        if (frame.getClass() == Connect.class)
        {
            System.out.println("protocol says: server got a connection request.");

            DataStorage.getInstance().getUsersByID().put(connectionID, this.user);

            String userName = ((Connect) frame).getHeaders()[2].split(":")[1];
            String password = ((Connect) frame).getHeaders()[3].split(":")[1];

            //case: username not exists. do in DataStorage: add to usersByID, add to users_passwords
            if (!DataStorage.getInstance().getUsers_Passwords().containsKey(userName))
            {
                System.out.println("debug: username not exists. do in DataStorage: add to usersByID, add to users_passwords");

                this.user = new User(true, userName, password, connectionID);
//                DataStorage.getInstance().getUsersByID().put(connectionID, this.user);
                DataStorage.getInstance().getUsers_Passwords().put(userName,password);
                Frame connected = new Connected(new String[]{"version:1.2"},"\u0000");
                this.connections.send(connectionID, connected);
            }
            else //case:username exists
            {
                System.out.println("debug:check if user already logged in (has a connection)");

                //check if user already logged in (has a connection)
                boolean found = false;
                for (User user: DataStorage.getInstance().getUsersByID().values()) {
                    if (user.getUserName().equals(userName)) {
                        found = true;
                        break;

                    }
                }
                if (found == true) //case: user already logged in
                {
                    Frame error = new Error(new String[]{"User already logged in","now - connection will be closed."},"");
                    this.connections.send(connectionID, error);
                    terminate();
                    this.connections.disconnect(connectionID);
                }
                else //case: user NOT logged in
                {
                    System.out.println("debug:case not login");

                    //check if password is correct
                    boolean passOk = true;
                    if (!DataStorage.getInstance().getUsers_Passwords().get(userName).equals(password)){
                        System.out.println("password is not correct");

                        passOk = false;}
                    System.out.println("passOk is: "+passOk);


                    if (!passOk) //case: pass not correct
                    {
                        System.out.println("password is not correct , and start to send error\nconnectionHandlerID: "+this.connectionID);

                        Frame error = new Error(new String[]{"receipt-id:"},"Wrong password, now - connection will be closed.");
                        this.connections.send(connectionID, error);
                        terminate();
                        this.connections.disconnect(connectionID);
                    }
                    else //case: pass is correct
                    {
                        this.user = new User(true, userName, password, connectionID);
                        DataStorage.getInstance().getUsersByID().put(connectionID, this.user);
                        Frame connected = new Connected(new String[]{"version:1.2"},"\u0000");
                        this.connections.send(connectionID, connected);
                    }
                }

            }
        } //end of handling a "CONNECT" frame
        else if (frame.getClass() == Disconnect.class)
        {

            String receiptID = ((Disconnect) frame).getHeaders()[0].split(":")[1];

            Frame receipt = new Receipt(new String[]{""},"");
            this.connections.send(connectionID, receipt);
            terminate();
            connections.disconnect(connectionID);
        } //end of handling a "DISCONNECT" frame
        else if (frame.getClass() == Subscribe.class)
        {

            String topic = ((Subscribe) frame).getHeaders()[0].split(":")[1];
            String subsID = ((Subscribe) frame).getHeaders()[1].split(":")[1]; //subscription ID
            String receiptID = ((Subscribe) frame).getHeaders()[2].split(":")[1];
            if(!DataStorage.getInstance().getTopicSubscribers().keySet().contains(topic))
            {
                DataStorage.getInstance().getTopicSubscribers().put(topic, new ArrayList<>());
            }
            if (this.user.getSubscriptionID().containsValue(topic)) return;
            this.user.getSubscriptionID().put(subsID,topic);
            DataStorage.getInstance().getTopicSubscribers().get(topic).add(this.user);
            Frame receipt = new Receipt(new String[]{"receipt-id:"+receiptID},"");
            this.connections.send(connectionID, receipt);
        } //end of handling a "SUBSCRIBE" frame
        else if (frame.getClass() == Unsubscribe.class)
        {

            String subsID = ((Unsubscribe) frame).getHeaders()[0].split(":")[1];
            String recRtrn = ((Unsubscribe) frame).getHeaders()[1].split(":")[1];


            if (!this.user.getSubscriptionID().containsKey(subsID)) return;
            String topic = this.user.getSubscriptionID().get(subsID);
            this.user.getSubscriptionID().remove(subsID);
            DataStorage.getInstance().getTopicSubscribers().get(topic).remove(this.user);
            Frame receipt = new Receipt(new String[]{"receipt-id:"+recRtrn},"");
            this.connections.send(connectionID, receipt);
        } //end of handling a "UNSUBSCRIBE" frame
        else if (frame.getClass() == Send.class)
        {
            String destination = ((Send) frame).getHeaders()[0].split(":")[1];

            String[] bodySplited = ((Send) frame).getBody().split("\\s+");

            String subsID = "sender not subscribed to this topic"; //default
            if (this.user.getSubscriptionID().containsValue(destination)) { //user is subscribed to the genre
                for (String id : user.getSubscriptionID().keySet()) {
                    if (user.getSubscriptionID().get(id) == destination) {
                        subsID = id;
                        break;
                    }
                }
            }
            //recognizing what type of "SEND" it is
            if (((Send) frame).getBody().contains("added")) //case: user added a book
            {
                if (!DataStorage.getInstance().getTopicSubscribers().containsKey(destination))//case: book is added to a genre which not exist
                {
                    DataStorage.getInstance().getTopicSubscribers().put(destination, new ArrayList<User>());
                }
                Frame message = new Message(new String[]{"subscription:"+subsID,"Message-id:"+messageIDCounter,"destination:"+destination},user.getUserName()+" has added the book "+bodySplited[bodySplited.length-1]); //TODO: CHECK UPDATE
                messageIDCounter++;

                connections.send(destination, message);
            }
            else if(((Send) frame).getBody().contains("wish to borrow")) //case: user wish to borrow a book
            {
                Frame message = new Message(new String[]{"subscription:"+subsID,"Message-id:"+messageIDCounter,"destination:"+destination}, user.getUserName()+" wish to borrow "+bodySplited[bodySplited.length-1]);
                messageIDCounter++;
                connections.send(destination, message);
            }
            else if(((Send) frame).getBody().contains("has")) //case: a user got the desired book
            {
                Frame message = new Message(new String[]{"subscription:"+subsID,"Message-id:"+messageIDCounter,"destination:"+destination}, bodySplited[0]+" has "+bodySplited[bodySplited.length-1]);
                messageIDCounter++;
                connections.send(destination, message);
            }
            else if(((Send) frame).getBody().contains("Taking")) //case: this is a "taking a book" message
            {
                Frame message2 = new Message(new String[]{"subscription:"+subsID,"Message-id:"+messageIDCounter,"destination:"+destination}, "Taking "+bodySplited[1]+ " from "+bodySplited[bodySplited.length-1]);
                connections.send(destination, message2);
                messageIDCounter++;
            }
            else if(((Send) frame).getBody().contains("Returning")) //case: Returning a book
            {
                Frame message = new Message(new String[]{"subscription:"+subsID,"Message-id:"+messageIDCounter,"destination:"+destination}, "Returning "+bodySplited[1]+" to "+bodySplited[bodySplited.length-1]);
                messageIDCounter++;
                connections.send(destination, message);
            }
            else if(((Send) frame).getBody().contains("status")) //case: requesting book status
            {
                Frame message = new Message(new String[]{"subscription:"+subsID,"Message-id:"+messageIDCounter,"destination:"+destination}, "book status");
                messageIDCounter++;
                connections.send(destination, message);
            }
            else //it is a "SEND" frame, which contains book status of the sender
            {
                if (!((Send) frame).getBody().contains(":")) return;
                Frame message = new Message(new String[]{"subscription:"+subsID,"Message-id:"+messageIDCounter,"destination:"+destination}, ((Send) frame).getBody());
                messageIDCounter++;
                connections.send(destination, message);
            }
        } //end of handling a "SEND" frame

    }

    public void terminate()
    {
        shouldTerminate = true;
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
