package bgu.spl.net.srv;

import bgu.spl.net.Frames.Frame;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl<T> implements Connections<T> {

    private int connectionCounter; //counts number of connected users from start - DON'T RESET TO 0!
    private ConcurrentHashMap<Integer, ConnectionHandler> connectionHandlers; //key: connection id. value: connection handler
    private DataStorage dataStorage;

    public ConnectionsImpl()
    {
        connectionCounter = 0;
        connectionHandlers = new ConcurrentHashMap<>();
        dataStorage = DataStorage.getInstance();
    }


    @Override
    public boolean send(int connectionId, T msg) {
        if (!connectionHandlers.containsKey(connectionId)) return false;

        connectionHandlers.get(connectionId).send(msg);
        return true;
    }

    @Override
    public void send(String channel, T msg) {
        if(!dataStorage.getTopicSubscribers().containsKey(channel)) return;

        for (User user:dataStorage.getTopicSubscribers().get(channel)) {
            connectionHandlers.get(user.getConnectID()).send(msg);
        }
    }

    @Override
    public void disconnect(int connectionId)  {
        //delete user from subscribed topics; close and delete connection handler; REMEMBER user name and password
        if(!dataStorage.getUsersByID().containsKey(connectionId)){
            System.out.println("connection id not found in connection handler map");
            return; //user not found
        }

        System.out.println("disconnect methos found handlerID: "+connectionId);
        User toRemove = dataStorage.getUsersByID().get(connectionId);
        dataStorage.getUsersByID().remove(connectionId);

        for (String topic: dataStorage.getTopicSubscribers().keySet()) { //for each topic
            if (dataStorage.getTopicSubscribers().get(topic).contains(toRemove))
                dataStorage.getTopicSubscribers().get(topic).remove(toRemove);
        }

    //    try {

           // connectionHandlers.get(connectionId).close();
            connectionHandlers.remove(connectionId);
       // }
/*        catch (IOException e)
        {
            e.printStackTrace();
        }*/
//        try
//        {
//            connectionHandlers.get(connectionId).close();
//            connectionHandlers.remove(connectionId);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
 /*       if (connectionHandlers.containsKey(connectionId))
        {
            try {
                connectionHandlers.get(connectionId).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            connectionHandlers.remove(connectionId);
        }
*/
    }

    public int addConnection(ConnectionHandler handler)
    {
        connectionHandlers.put(++connectionCounter, handler);
//        DataStorage.getInstance().getUsersByID().put(connectionID, this.user);
        return connectionCounter;
    }
}
