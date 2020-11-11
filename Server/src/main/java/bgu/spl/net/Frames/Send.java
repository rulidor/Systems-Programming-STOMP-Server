package bgu.spl.net.Frames;

public class Send extends Frame {
    public Send(String[] headers, String body) {
        super("SEND", headers, body);
    }
}
