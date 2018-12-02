package com.mitix.chapter_01_02.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author oldflame-jm
 * @create 2018/6/6
 * ${DESCRIPTION}
 */
public class TimeClient4Bio {
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            socket = new Socket("127.0.0.1", 8080);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("query time order");
            System.out.println("Send order 2 server succeed.");
            String resp = reader.readLine();
            System.out.println("Now is :" + resp);
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
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
