package com.mitix.chapter_01_02.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author oldflame-jm
 * @create 2018/5/30
 * ${DESCRIPTION}
 */
public class BioServer {
    private final static int port = 10086;
    private final static int buff_size = 1024;

    @SuppressWarnings("resource")
    public static void main(String[] args) throws IOException {
        //创建socket服务
        ServerSocket server = new ServerSocket(port);
        System.out.println("启动了socket服务...");
        while (true) {
            final Socket socket = server.accept();
            System.out.println("有新的客户端进行连接...");
            handler(socket);
        }
    }

    private static void handler(Socket socket) throws IOException {
        try {
            byte[] bytes = new byte[buff_size];
            InputStream inputStream = socket.getInputStream();
            while (true) {
                int read = inputStream.read(bytes);
                if (read != -1) {
                    System.out.println(new String(bytes, 0, read));
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭socket...");
            socket.close();
        }
    }
}
