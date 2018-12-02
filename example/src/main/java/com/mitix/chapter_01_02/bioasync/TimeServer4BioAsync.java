package com.mitix.chapter_01_02.bioasync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author oldflame-jm
 * @create 2018/6/4
 * ${DESCRIPTION}
 */
public class TimeServer4BioAsync {
    private static int port = 8080;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("timeserver is start in port :" + port);
            Socket socket = null;

            ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 6, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(10));
            while (true) {
                socket = serverSocket.accept();
                executor.execute(new TimeServerHandler(socket));
            }
        } finally {
            if (serverSocket != null) {
                System.out.println("close server");
                serverSocket.close();
            }
        }
    }

    private static class TimeServerHandler implements Runnable {
        private Socket socket;

        public TimeServerHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            BufferedReader reader = null;
            PrintWriter writer = null;
            try {
                reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                writer = new PrintWriter(this.socket.getOutputStream(), true);
                String currentTime = null;
                String body = null;
                while (true) {
                    body = reader.readLine();
                    if (body == null) {
                        break;
                    }
                    System.out.println("the time server receive order :" + body);
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                    currentTime = "query time order".equalsIgnoreCase(body) ? format.format(new Date()) : "bad order";
                    writer.println(currentTime);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (writer != null) {
                    writer.close();
                }
                if (this.socket != null) {
                    try {
                        this.socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
