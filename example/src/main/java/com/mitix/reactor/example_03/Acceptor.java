package com.mitix.reactor.example_03;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author oldflame-jm
 * @create 2018/11
 * @since
 */
public class Acceptor implements Runnable {
    final Selector selector;
    final ServerSocketChannel ssChannel;

    public Acceptor(Selector selector, ServerSocketChannel ssChannel) {
        this.selector = selector;
        this.ssChannel = ssChannel;
    }

    @Override
    public void run() {
        try {
            SocketChannel sc = ssChannel.accept();
            System.out.println(sc.socket().getRemoteSocketAddress().toString() + " is connected.");
            if (sc != null) {
                sc.configureBlocking(false);
                SelectionKey sk = sc.register(selector, SelectionKey.OP_READ);
                selector.wakeup();
                sk.attach(new Handler(sk, sc));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
