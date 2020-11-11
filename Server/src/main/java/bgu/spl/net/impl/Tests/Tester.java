/*
package bgu.spl.net.impl.Tests;

import bgu.spl.net.Frames.Connect;
import bgu.spl.net.Frames.Frame;
import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessageEncoderDecoderImpl;
import bgu.spl.net.api.StompMessagingProtocolImpl;
import bgu.spl.net.impl.echo.EchoClient;
import bgu.spl.net.impl.rci.ObjectEncoderDecoder;
import bgu.spl.net.impl.rci.RemoteCommandInvocationProtocol;
import bgu.spl.net.srv.Server;

public class Tester {

    public static void main(String[] args) {






        //---------------------ENCODING & DECODING
//        Frame frame = new Connect(new String[]{"accept-version:1.2", "host:stomp.cs.bgu.ac.il", "login:bob", "passcode:alice"},"");
//        System.out.println(frame);
//        MessageEncoderDecoderImpl messageEncoderDecoder = new MessageEncoderDecoderImpl();
//        System.out.println("-----ENcoding result:-----");
//        for (byte b:messageEncoderDecoder.encode(frame)) {
//            System.out.println(b);
//        }
//
//        System.out.println("-----DEcoding result:-----");
//
//        Frame res=null;
//        for (byte b:messageEncoderDecoder.bytes) {
//            res = messageEncoderDecoder.decodeNextByte(b);
//        }
//        System.out.println(res);







        //---------------------REACTOR
    */
/*    Server.reactor(
                Runtime.getRuntime().availableProcessors(),
                7777, //port
                () ->  new StompMessagingProtocolImpl(), //protocol factory
                MessageEncoderDecoderImpl::new //message encoder decoder factory
        ).serve();*//*




        //---------------------THREAD PER CLIENT

                Server.threadPerClient(
                7777, //port
                () -> new StompMessagingProtocolImpl(), //protocol factory
                        MessageEncoderDecoderImpl::new //message encoder decoder factory
        ).serve();

    }
}
*/
