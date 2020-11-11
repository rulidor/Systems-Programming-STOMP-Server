package bgu.spl.net.Frames;

public class Message extends Frame {
    public Message(String[] headers, String body) {
        super("MESSAGE", headers, body);
    }
}
