package bgu.spl.net.Frames;

public class Connected extends Frame{
    public Connected(String[] headers, String body) {
        super("CONNECTED", headers, body);
    }
}
