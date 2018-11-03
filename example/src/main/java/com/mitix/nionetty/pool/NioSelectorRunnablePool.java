package com.mitix.nionetty.pool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.mitix.nionetty.NioServerBoss;
import com.mitix.nionetty.NioServerWorker;

/**
 * selector线程管理者
 *
 * @author -琴兽-
 */
public class NioSelectorRunnablePool {

    /**
     * boss线程数组
     */
    private final AtomicInteger bossIndex = new AtomicInteger();
    private Boss[] bosses;
    /**
     * worker线程数组
     */
    private final AtomicInteger workerIndex = new AtomicInteger();
    private Worker[] workeres;


    public NioSelectorRunnablePool() {
        //线程池
        ThreadPoolExecutor bossExecutor = new ThreadPoolExecutor(3, 20, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(10));
        ThreadPoolExecutor workExecutor = new ThreadPoolExecutor(3, 20, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(10));

        initBoss(bossExecutor, 1);
        initWorker(workExecutor, 1);
        //initWorker(worker, Runtime.getRuntime().availableProcessors() * 2);
    }

    /**
     * 初始化boss线程
     *
     * @param bossExecutor
     * @param count
     */
    private void initBoss(Executor bossExecutor, int count) {
        this.bosses = new Boss[count];
        for (int i = 0; i < bosses.length; i++) {
            bosses[i] = new NioServerBoss(bossExecutor, "boss thread " + (i + 1), this);
        }
    }

    /**
     * 初始化worker线程
     *
     * @param worker
     * @param count
     */
    private void initWorker(Executor worker, int count) {
        this.workeres = new NioServerWorker[count];
        for (int i = 0; i < workeres.length; i++) {
            workeres[i] = new NioServerWorker(worker, "worker thread " + (i + 1), this);
        }
    }

    /**
     * 获取一个worker
     *
     * @return
     */
    public Worker nextWorker() {
        return workeres[Math.abs(workerIndex.getAndIncrement() % workeres.length)];
    }

    /**
     * 获取一个boss
     *
     * @return
     */
    public Boss nextBoss() {
        return bosses[Math.abs(bossIndex.getAndIncrement() % bosses.length)];
    }

}
