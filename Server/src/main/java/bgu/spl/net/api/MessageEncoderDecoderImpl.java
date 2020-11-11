package bgu.spl.net.api;

import bgu.spl.net.Frames.*;
import bgu.spl.net.Frames.Error;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageEncoderDecoderImpl implements MessageEncoderDecoder<Frame> {
    public byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;

    @Override
    public Frame decodeNextByte(byte nextByte) {
        //notice that the top 128 ascii characters have the same representation as their utf-8 counterparts
        //this allow us to do the following comparison
        if (nextByte == '\u0000') {
            return popString();
        }

        pushByte(nextByte);
        return null; //not a line yet
    }

    @Override
    public byte[] encode(Frame message) {
        return (message.toString() + '\u0000').getBytes(); //uses utf8 by default
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    public Frame popString() {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        String resString = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        Frame result = null;
        System.out.println("*********Decoder got: \n"+resString);



        String[] splited = resString.split("\n");

        List<String> headers = new ArrayList<>();
        //taking care of headers, starting from splited[1]
        int i=1;
        while(i<splited.length && splited[i].length()>1) //not a blank line
        {
            headers.add(splited[i]);
            i++;
        }
        System.out.println("middle: \n"+headers.toString());

        String[] finalHeaders = new String[headers.size()];
        i=0;
        for (String header:headers) {
            finalHeaders[i] = header;
            i++;
        }

        //taking care of the body
        String body="";
        for (int j=i+1; j<splited.length; j++)
            body+=splited[j];

        switch (splited[0])
        {
            case "CONNECT":
                result = new Connect(finalHeaders, body);
                break;
            case "DISCONNECT":
                result = new Disconnect(finalHeaders, body);
                break;
            case "SEND":
                result = new Send(finalHeaders, body);
                break;
            case "SUBSCRIBE":
                result = new Subscribe(finalHeaders, body);
                break;
            case "UNSUBSCRIBE":
                result = new Unsubscribe(finalHeaders, body);
                break;
            default: //case error in message
                result = new Error(finalHeaders, body);
        }

        System.out.println("EncDec final output: \n"+result.toString());
        return result;
    }
}
