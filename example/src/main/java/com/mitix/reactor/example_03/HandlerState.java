package com.mitix.reactor.example_03;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author oldflame-jm
 * @create 2018/11
 * @since
 */
public interface HandlerState {
    void handle(Handler handler, SelectionKey sk, SocketChannel sc, ThreadPoolExecutor pool) throws IOException;

    void changeState(Handler handler);
}
