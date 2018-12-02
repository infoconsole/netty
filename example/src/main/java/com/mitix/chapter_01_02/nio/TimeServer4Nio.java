package com.mitix.chapter_01_02.nio;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author oldflame-jm
 * @create 2018/6/4
 * ${DESCRIPTION}
 */
public class TimeServer4Nio {
    private static int port = 8080;

    public static void main(String[] args) throws IOException {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 10, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);

        executor.execute(timeServer);

    }

}
