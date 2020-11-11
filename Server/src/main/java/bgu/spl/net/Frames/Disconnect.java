package bgu.spl.net.Frames;

public class Disconnect extends Frame {
    public Disconnect(String[] headers, String body) {
        super("DISCONNECT", headers, body);
    }
}
