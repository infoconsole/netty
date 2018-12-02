package com.mitix.chapter_01_02.bioasync;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 6, 5, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());

        //创建socket服务
        ServerSocket server = new ServerSocket(port);
        System.out.println("启动了socket服务...");
        while (true) {
            final Socket socket = server.accept();
            System.out.println("有新的客户端进行连接...socket is " + socket);
            //同步异步处理方式1
//            executor.execute(new Runnable() {
//                @Override
//                public void run() {
//                    handler(socket);
//                }
//            });
            //同步异步处理方式2
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    handler2(socket);
                }
            });
        }
    }


    private static void handler(Socket socket) {
        try {
            byte[] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            System.out.println("服务端获取stream...");
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
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 传统的Bio主要的阻塞是在读取数据流的时候没有异步
     * 表现在一个SocketServer可以连接很多的socket
     * 但是在一个socket没有结束流数据读取的时候，后续的线程都是无法进行数据读取的
     * 被阻塞在数据读取的地方
     * 看handler handler2处理的不同
     * 阻塞在socket.getInputStream()上
     *
     * @param socket
     */
    private static void handler2(Socket socket) {
        try {
            byte[] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            System.out.println("服务端获取stream...");
            int read = inputStream.read(bytes);
            if (read != -1) {
                System.out.println(new String(bytes, 0, read));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭socket...");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
