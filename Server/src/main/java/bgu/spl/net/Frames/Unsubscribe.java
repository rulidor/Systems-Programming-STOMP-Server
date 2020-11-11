package bgu.spl.net.Frames;

public class Unsubscribe extends Frame {
    public Unsubscribe(String[] headers, String body) {
        super("UNSUBSCRIBE", headers, body);
    }
}
