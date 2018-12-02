package com.mitix.reactor.example_03;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author oldflame-jm
 * @create 2018/11
 * @since
 */
public class WriteState implements HandlerState {
    public WriteState() {
    }

    @Override
    public void changeState(Handler handler) {
        handler.setState(new ReadState());
    }

    @Override
    public void handle(Handler handler, SelectionKey sk, SocketChannel sc,
                       ThreadPoolExecutor pool) throws IOException {

        String str = "Your message has sent to "
                + sc.socket().getLocalSocketAddress().toString() + "\r\n";
        ByteBuffer buf = ByteBuffer.wrap(str.getBytes());

        while (buf.hasRemaining()) {
            sc.write(buf);
        }

        handler.setState(new ReadState());
        sk.interestOps(SelectionKey.OP_READ);
        sk.selector().wakeup();
    }
}
