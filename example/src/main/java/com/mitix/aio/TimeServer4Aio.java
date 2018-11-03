package com.mitix.aio;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author oldflame-Jm
 * @version 1.0
 * @date 2014年2月14日
 */
public class TimeServer4Aio {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 10, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

        executor.execute(new AsyncTimeServerHandler(8080));
    }
}
