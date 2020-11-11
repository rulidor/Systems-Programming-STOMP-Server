package bgu.spl.net.Frames;

public class Subscribe extends Frame {
    public Subscribe(String[] headers, String body) {
        super("SUBSCRIBE", headers, body);
    }
}
