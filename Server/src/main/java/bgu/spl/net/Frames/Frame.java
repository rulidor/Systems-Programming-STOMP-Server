package bgu.spl.net.Frames;

import java.util.Arrays;
import java.util.HashMap;

public abstract class Frame {
    protected String command;
    protected String[] headers;
    protected String body;

    public Frame(String command, String[] headers, String body) {
        this.command = command;
        this.headers = headers;
        this.body = body;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String[] getHeaders() {
        return headers;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return
                ""+command + "\n" +
                arrayToString(headers) + "\n" +
                body + "\n" +
                "^@";
    }

    private String arrayToString(String[] arr)
    {
        String res="";
        for (String h: headers) {
            res+=h + "\n";
        }
        return res;
    }
}
