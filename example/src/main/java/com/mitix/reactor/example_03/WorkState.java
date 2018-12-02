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
public class WorkState implements HandlerState {

    public WorkState() {
    }

    @Override
    public void changeState(Handler handler) {
        // TODO Auto-generated method stub
        handler.setState(new WriteState());
    }

    @Override
    public void handle(Handler handler, SelectionKey sk, SocketChannel sc,
                       ThreadPoolExecutor pool) throws IOException {
        // TODO Auto-generated method stub

    }
}
