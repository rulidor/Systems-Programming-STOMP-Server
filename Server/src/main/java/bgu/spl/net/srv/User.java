package bgu.spl.net.srv;

import java.util.concurrent.ConcurrentHashMap;

public class User {

    private boolean isActive;
    private String userName, password;
    private int connectID; //as defines in Connections
    private ConcurrentHashMap<String, String> subscriptionID; //key: subscription request ID. value: topic


    public User()
    {
        this.isActive = false;
        this.userName = "";
        this.password = "";
        this.connectID = -1;
        this.subscriptionID = new ConcurrentHashMap<>();
    }

    public User(boolean isActive, String userName, String password, int connectID) {
        this.isActive = isActive;
        this.userName = userName;
        this.password = password;
        this.connectID = connectID;
        this.subscriptionID = new ConcurrentHashMap<>();
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getConnectID() {
        return connectID;
    }

    public void setConnectID(int connectID) {
        this.connectID = connectID;
    }

    public ConcurrentHashMap<String, String> getSubscriptionID() {
        return subscriptionID;
    }

    public void setSubscriptionID(ConcurrentHashMap<String, String> subscriptionID) {
        this.subscriptionID = subscriptionID;
    }

    @Override
    public String toString() {
        return "User{" +
                "isActive=" + isActive +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", connectID='" + connectID + '\'' +
                ", subscriptionID=" + subscriptionID +
                '}';
    }
}
