package com.mitix.aio;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author oldflame-Jm
 * @version 1.0
 * @date 2014年2月14日
 */
public class TimeClient4Aio {

    /**
     * @param args
     */
    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 10, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

        executor.execute(new AsyncTimeClientHandler("127.0.0.1", 8080));

    }
}
