package bgu.spl.net.Frames;

public class Connect extends Frame{
    public Connect(String[] headers, String body) {
        super("CONNECT", headers, body);
    }
}
