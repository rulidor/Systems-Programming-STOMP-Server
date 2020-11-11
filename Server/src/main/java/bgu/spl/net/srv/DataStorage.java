package bgu.spl.net.srv;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class DataStorage {

    private static DataStorage dataInstance;
    private ConcurrentHashMap<String, ArrayList<User>> topicSubscribers; //key: topic. value: User that is subscribed
    private ConcurrentHashMap<Integer, User> usersByID; //key: connectID. value: User
    private ConcurrentHashMap<String, String> users_passwords; //key: user name. value: password


    private DataStorage()
    {
        topicSubscribers = new ConcurrentHashMap<>();
        usersByID = new ConcurrentHashMap<>();
        users_passwords = new ConcurrentHashMap<>();
    }

    public static DataStorage getInstance(){
        if (dataInstance==null)
            dataInstance = new DataStorage();
        return dataInstance;
    }

    public ConcurrentHashMap<String, ArrayList<User>> getTopicSubscribers() {
        return topicSubscribers;
    }

    public void setTopicSubscribers(ConcurrentHashMap<String, ArrayList<User>> topicSubscribers) {
        this.topicSubscribers = topicSubscribers;
    }

    public ConcurrentHashMap<Integer, User> getUsersByID() {
        return usersByID;
    }

    public void setUsersByID(ConcurrentHashMap<Integer, User> usersByID) {
        this.usersByID = usersByID;
    }

    public ConcurrentHashMap<String, String> getUsers_Passwords() {
        return users_passwords;
    }

    public void setUsers_Passwords(ConcurrentHashMap<String, String> passwords) {
        this.users_passwords = passwords;
    }
}
