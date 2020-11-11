package bgu.spl.net.impl.echo;

import bgu.spl.net.Frames.Connect;
import bgu.spl.net.Frames.Disconnect;
import bgu.spl.net.Frames.Frame;
import bgu.spl.net.Frames.Send;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class EchoClient {

    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            args = new String[]{"localhost", "CONNECT"+'\u0000'};
        }

        if (args.length < 2) {
            System.out.println("you must supply two arguments: host, message");
            System.exit(1);
        }

        //BufferedReader and BufferedWriter automatically using UTF-8 encoding
        try (Socket sock = new Socket(args[0], 7777);
                BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()))) {

            Frame msg = new Connect(new String[]{"1.2", "stomp.cs.bgu.ac.il","bob", "alice"},"book status\u0000");
            System.out.println("sending message to server");
//            for(int i=0; i<outStr.length;i++)
//                out.write(outStr[i]);
            System.out.println("*******client sends the Frame:\n"+msg.toString());
            out.write(msg.toString());
            out.newLine();
            out.flush();

            System.out.println("awaiting response");
            String line = in.readLine();
            System.out.println("message from server: " + line);
        }
    }
}
