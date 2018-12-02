package com.mitix.reactor.example_01;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

/**
 * @author oldflame-jm
 * @create 2018/11
 * @since
 */
public class Handler implements Runnable {
    final Socket socket;

    public Handler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            byte[] input = new byte[1024];
            socket.getInputStream().read(input);
            byte[] output = process(input);
            socket.getOutputStream().write(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] process(byte[] input) throws UnsupportedEncodingException {
        String str = new String(input, "utf-8");
        System.out.println(str);
        String outstr = "receive is " + str + " and say rev haha ";
        return outstr.getBytes("utf-8");
    }
}
