package com.mitix.chapter_01_02.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author oldflame-jm
 * @create 2018/6/3
 * ${DESCRIPTION}
 */
public class NIOClient {
    private Selector selector;
    private final static int port = 10086;
    private final static int buff_size = 1024;

    public static void main(String[] args) throws IOException {
        NIOClient client = new NIOClient();
        client.init("localhost", port);
        client.connect();
    }

    public NIOClient() throws IOException {
        this.selector = Selector.open();
    }

    private void init(String address, int port) throws IOException {
        //客户端，首先有一个SocketChannel
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);//将此通道设置为非阻塞模式
        //连接
        socketChannel.connect(new InetSocketAddress(address, port));

        //将SocketChannel注册到selector中，并为该通道注册SelectionKey.OP_CONNECT
        socketChannel.register(selector, SelectionKey.OP_CONNECT);


    }


    private void connect() throws IOException {
        int data = 1;
        while (true) {
            selector.select();//
            Set<SelectionKey> set = selector.selectedKeys();
            Iterator<SelectionKey> ite = set.iterator();

            while (ite.hasNext()) {
                SelectionKey selectionKey = (SelectionKey) ite.next();
                //删除已选的key,以防重复处理
                ite.remove();
                //看是否有连接发生
                if (selectionKey.isConnectable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    //如果正在连接，则完成连接
                    if (socketChannel.isConnectionPending()) {
                        socketChannel.finishConnect();
                    }
                    //设置为非阻塞模式
                    socketChannel.configureBlocking(false);
                    //给服务器端发送数据
                    System.out.println("客户端连接上了服务器端。。。。");
                    socketChannel.write(ByteBuffer.wrap(new String("hello server!").getBytes()));
                    //为了接收来自服务器端的数据，将此通道注册到选择器中
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    //接收来自于服务器端发送过来的数据
                    ByteBuffer buf = ByteBuffer.allocate(buff_size);
                    int len = 0;
                    while ((len = socketChannel.read(buf)) > 0) {
                        byte[] receData = buf.array();
                        String msg = new String(receData).trim();
                        System.out.println("接收来自服务器端的数据为：" + msg);
                        buf.clear();
                    }
                    //发送数据
                    buf.flip();
                    while (buf.hasRemaining()) {
                        socketChannel.write(buf);
                    }
                    buf.compact();
                    data++;
                }


            }

        }

    }
}