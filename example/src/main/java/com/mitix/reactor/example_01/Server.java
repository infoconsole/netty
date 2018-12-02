package com.mitix.reactor.example_01;


import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author oldflame-jm
 * @create 2018/11
 * @since
 */
public class Server implements Runnable {
    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(8080);
            while (!Thread.interrupted()) {
                new Thread(new Handler(ss.accept())).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}
