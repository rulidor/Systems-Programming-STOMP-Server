package bgu.spl.net.Frames;

public class Error extends Frame {
    public Error(String[] headers, String body) {
        super("ERROR", headers, body);
    }
}
