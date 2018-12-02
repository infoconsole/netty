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
public class ReadState implements HandlerState {
    private SelectionKey sk;

    public ReadState() {

    }

    @Override
    public void handle(Handler handler, SelectionKey sk, SocketChannel sc, ThreadPoolExecutor pool) throws IOException {
        this.sk = sk;
        byte[] arr = new byte[1024];
        ByteBuffer buf = ByteBuffer.wrap(arr);

        int numBytes = sc.read(buf); // 讀取字符串
        if (numBytes == -1) {
            System.out.println("[Warning!] A client has been closed.");
            handler.closeChannel();
            return;
        }
        String str = new String(arr);
        if ((str != null) && !str.trim().equals("")) {
            System.out.println("change to WorkState now ");
            handler.setState(new WorkState());
            pool.execute(new WorkerThread(handler, str));
            System.out.println(sc.socket().getRemoteSocketAddress().toString()
                    + " > " + str);
        }
    }

    @Override
    public void changeState(Handler handler) {
        handler.setState(new WorkState());
    }


    class WorkerThread implements Runnable {

        Handler handler;
        String str;

        public WorkerThread(Handler handler, String str) {
            this.handler = handler;
            this.str = str;
        }

        @Override
        public void run() {
            process(handler, str);
        }

        synchronized void process(Handler handler, String str) {
            // do process(decode, logically process, encode)..
            handler.setState(new WriteState());
            sk.interestOps(SelectionKey.OP_WRITE);
            sk.selector().wakeup();
        }

    }
}
