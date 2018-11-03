package com.mitix.nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * @author oldflame-jm
 * @create 2018/6/7
 * ${DESCRIPTION}
 */
public class MultiplexerTimeServer implements Runnable {

    private int port;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private volatile boolean stop;

    public MultiplexerTimeServer(int port) {
        this.port = port;
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port), 1024);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("time server is start...4prot is " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key = null;
                while (it.hasNext()) {
                    key = it.next();
                    it.remove();
                    try {
                        handleInput(key);
                    } catch (Exception e) {
                        if (key != null && key.channel() != null) {
                            key.channel().close();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (selector != null) {
                    try {
                        selector.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            // 处理新接入的请求消息
            if (key.isAcceptable()) {
                //accept a new connection
                ServerSocketChannel serverchannel = (ServerSocketChannel) key.channel();
                SocketChannel channel = serverchannel.accept();
                channel.configureBlocking(false);
                channel.register(selector, SelectionKey.OP_READ);
            } else if (key.isReadable()) {
                // read data
                SocketChannel socketChannel = (SocketChannel) key.channel();
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int lens = socketChannel.read(buffer);
                if (lens > 0) {
                    buffer.flip();
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    String body = new String(bytes, "UTF-8").trim();
                    System.out.println("the time server receive order :" + body);
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                    String currentTime = "query time order".equalsIgnoreCase(body) ? format.format(new Date()) : "bad order";
                    doWirte(socketChannel, currentTime);
                }
            }
        }
    }

    private void doWirte(SocketChannel socketChannel, String currentTime) throws IOException {
        if (socketChannel != null && currentTime != null && currentTime.length() > 0) {
            ByteBuffer buffer = ByteBuffer.allocate(currentTime.getBytes().length);
            buffer.put(currentTime.getBytes());
            buffer.flip();
            socketChannel.write(buffer);
            buffer.compact();
            System.out.printf("已经写入...+" + currentTime);
        }
    }

}
