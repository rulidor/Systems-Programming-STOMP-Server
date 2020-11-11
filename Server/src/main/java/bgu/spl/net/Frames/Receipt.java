package bgu.spl.net.Frames;

public class Receipt extends Frame {
    public Receipt(String[] headers, String body) {
        super("RECEIPT", headers, body);
    }
}
