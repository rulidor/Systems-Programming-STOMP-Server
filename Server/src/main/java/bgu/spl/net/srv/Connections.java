package bgu.spl.net.srv;

import bgu.spl.net.Frames.Frame;

import java.io.IOException;

public interface Connections<T> {

    boolean send(int connectionId, T msg);

    void send(String channel, T msg);

    void disconnect(int connectionId) throws IOException;

    int addConnection(ConnectionHandler handler);
}
