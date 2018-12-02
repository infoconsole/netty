package com.mitix.chapter_01_02.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author oldflame-jm
 * @create 2018/5/30
 * ${DESCRIPTION}
 */
public class NioServer {
    /**
     * 选择器
     */
    private Selector selector;
    private final static int port = 10086;
    private final static int buff_size = 1024;
    private int index = 0;

    private void initServer() throws IOException {
        //创建通道管理器对象selector
        this.selector = Selector.open();

        //既然是服务器端，肯定需要一个ServerSocketChannel来监听新进来的TCP连接
        ServerSocketChannel channel = ServerSocketChannel.open();
        //将通道设置为非阻塞
        channel.configureBlocking(false);
        //将通道绑定在10086端口
        channel.socket().bind(new InetSocketAddress(port));

        //将上述的通道管理器和通道绑定，并为该通道注册OP_ACCEPT事件
        //注册事件后，当该事件到达时，selector.select()会返回（一个key），如果该事件没到达selector.select()会一直阻塞
        SelectionKey selectionKey = channel.register(selector, SelectionKey.OP_ACCEPT);

        //轮询
        while (true) {
            //这是一个阻塞方法，一直等待直到有数据可读，返回值是key的数量（可以有多个）
            selector.select();
            System.out.println("通过select阻塞...");
            //如果channel有数据了，将生成的key访入keys集合中
            Set keys = selector.selectedKeys();
            //得到这个keys集合的迭代器
            Iterator iterator = keys.iterator();
            //使用迭代器遍历集合
            while (iterator.hasNext()) {
                //得到集合中的一个key实例
                SelectionKey key = (SelectionKey) iterator.next();
                //拿到当前key实例之后记得在迭代器中将这个元素删除，非常重要，否则会出错
                iterator.remove();
                //判断当前key所代表的channel是否在Acceptable状态，如果是就进行接收
                if (key.isAcceptable()) {
                    doAccept(key);
                } else if (key.isReadable()) {
                    doRead(key);
                } else if (key.isWritable() && key.isValid()) {
                    doWrite(key, "根");
                } else if (key.isConnectable()) {
                    System.out.println("连接成功！");
                }
            }
        }
    }

    public void doAccept(SelectionKey key) throws IOException {
        //先拿到这个SelectionKey里面的ServerSocketChannel
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        // 获得和客户端连接的通道
        SocketChannel clientChannel = serverChannel.accept();
        System.out.println("有客户端连接到服务器！！！");
        clientChannel.configureBlocking(false);
        //向客户端发送数据
        clientChannel.write(ByteBuffer.wrap(new String("hello client!").getBytes()));



        //为了接收客户端发送过来的数据，需要将此通道绑定到选择器上，并为该通道注册读事件
        clientChannel.register(key.selector(), SelectionKey.OP_READ|SelectionKey.OP_WRITE);
    }

    public void doRead(SelectionKey key) throws IOException {
        //先拿到这个SelectionKey里面的SocketChannel
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(buff_size);

        long bytesRead = 0;
        while ((bytesRead = clientChannel.read(byteBuffer)) > 0) {
            byteBuffer.flip();
            byte[] data = byteBuffer.array();
            String info = new String(data).trim();
            System.out.println("从客户端发送过来的消息是：" + info);
            byteBuffer.clear();
        }

        doWrite(key, "收到数据" + index++);
//        byteBuffer.put("收到信息".getBytes());
//        while (byteBuffer.hasRemaining()) {
//            clientChannel.write(byteBuffer);
//        }

    }

    public void doWrite(SelectionKey key, String msg) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(buff_size);
        byteBuffer.flip();
        SocketChannel clientChannel = (SocketChannel) key.channel();
        while (byteBuffer.hasRemaining()) {
            clientChannel.write(byteBuffer);
        }
        byteBuffer.compact();
    }

    public static void main(String[] args) throws IOException {
        NioServer nioServer = new NioServer();
        nioServer.initServer();
    }

}
