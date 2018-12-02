package com.mitix.reactor.example_03;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author oldflame-jm
 * @create 2018/11
 * @since
 */
public class Reactor implements Runnable {
    final Selector selector;
    final ServerSocketChannel ssChannel;

    public Reactor(int port) throws IOException {
        selector = Selector.open();
        ssChannel = ServerSocketChannel.open();
        ssChannel.socket().bind(new InetSocketAddress(port));
        ssChannel.configureBlocking(false);
        Acceptor acceptor = new Acceptor(selector, ssChannel);
        System.out.println("this acceptor is " + acceptor);
        ssChannel.register(selector, SelectionKey.OP_ACCEPT, acceptor);
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
                dispatch(key);
                keyIterator.remove();
            }
        }
    }

    private void dispatch(SelectionKey key) {
        Runnable r = (Runnable) key.attachment();
        System.out.println("this attachment is " + r);
        if (r != null) {
            r.run();
        }
    }
}
