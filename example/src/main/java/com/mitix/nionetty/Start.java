package com.mitix.nionetty;

import com.mitix.nionetty.pool.NioSelectorRunnablePool;

import java.net.InetSocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 启动函数
 *
 * @author -琴兽-
 */
public class Start {

    public static void main(String[] args) {


        //初始化线程
        NioSelectorRunnablePool nioSelectorRunnablePool = new NioSelectorRunnablePool();

        //获取服务类
        ServerBootstrap bootstrap = new ServerBootstrap(nioSelectorRunnablePool);

        //绑定端口
        bootstrap.bind(new InetSocketAddress(10101));

        System.out.println("start");
    }

}
