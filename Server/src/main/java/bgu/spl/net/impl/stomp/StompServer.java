package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.MessageEncoderDecoderImpl;
import bgu.spl.net.api.StompMessagingProtocolImpl;
import bgu.spl.net.impl.echo.LineMessageEncoderDecoder;
import bgu.spl.net.srv.Server;

public class StompServer {

    public static void main(String[] args) {
        if (args[1].contentEquals("tpc")) {
            Server.threadPerClient(Integer.valueOf(args[0]),
                    () -> new StompMessagingProtocolImpl(),
                    MessageEncoderDecoderImpl::new).serve();
        } else {
            Server.reactor(
                    Runtime.getRuntime().availableProcessors(),
                    Integer.valueOf(args[0]), //port
                    () ->  new StompMessagingProtocolImpl(), //protocol factory
                    MessageEncoderDecoderImpl::new //message encoder decoder factory
            ).serve();//*
        }

    }


}

