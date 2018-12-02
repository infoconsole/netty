package com.mitix.reactor.example_02;


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
 * @create 2018/11
 * @since Initiation Dispatcher
 * 初始分发器   也就是Reactor的角色
 */
public class Reactor implements Runnable {
    /**
     * Synchronous Event Demultiplexer
     */
    final Selector selector;
    /**
     * Handle
     */
    final ServerSocketChannel ssChannel;

    public Reactor(int port) throws IOException {
        selector = Selector.open();
        ssChannel = ServerSocketChannel.open();
        ssChannel.socket().bind(new InetSocketAddress(port));
        ssChannel.configureBlocking(false);
        //handle注册到Synchronous Event Demultiplexer上，以监听channel发生的事件
        SelectionKey sk = ssChannel.register(selector, SelectionKey.OP_ACCEPT, this);
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                if (selector.select() == 0) {
                    continue;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                System.out.println("port " + ssChannel.socket().getLocalPort() + " ... event is " + key.interestOps());
                try {
                    dispatch(key);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            selectionKeys.clear();
        }
    }

    private void dispatch(SelectionKey key) throws IOException {
        // 客户端请求连接事件
        if (key.isAcceptable()) {
            handlerAccept(key);
        } else if (key.isReadable()) {
            handlerRead(key);
        } else if (key.isWritable()) {
            handlerWrite(key);
        }
    }

    private void handlerWrite(SelectionKey key) throws IOException {
        SocketChannel sc = null;
        try {
            sc = (SocketChannel) key.channel();
            String str = "Your message has sent to "
                    + sc.socket().getLocalSocketAddress().toString() + "\r\n";

            ByteBuffer buf = ByteBuffer.wrap(str.getBytes());
            while (buf.hasRemaining()) {
                sc.write(buf);
            }
            buf.compact();
            key.interestOps(SelectionKey.OP_READ);
            selector.wakeup();
        } catch (IOException e) {
            e.printStackTrace();
            if (sc != null) {
                sc.close();
            }
        }
    }

    private void handlerRead(SelectionKey key) throws IOException {
        byte[] arr = new byte[1024];
        ByteBuffer buf = ByteBuffer.wrap(arr);

        SocketChannel sc = (SocketChannel) key.channel();
        int numBytes = sc.read(buf);
        if (numBytes == -1) {
            System.out.println("[Warning!] A client has been closed.");
            return;
        }
        String str = new String(arr);
        if ((str != null) && !str.trim().equals("")) {
            System.out.println(sc.socket().getRemoteSocketAddress().toString()
                    + " > " + str);
        }
        sc.register(selector, SelectionKey.OP_WRITE, this);
        key.interestOps(SelectionKey.OP_WRITE);
        selector.wakeup();
    }

    private void handlerAccept(SelectionKey key) throws IOException {
        SocketChannel sc = ((ServerSocketChannel) key.channel()).accept();
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_READ, this);
    }
}
