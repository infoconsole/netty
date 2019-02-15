package com.mitix.netty.dcoder.lengthfield;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class StickyDemoClient {
    private static final String host = "127.0.0.1";
    private static final int port = 8080;

    public static void main(String[] args) throws Exception {
        new StickyDemoClient().start();
    }

    public void start() throws Exception {
        // 在客户端,没有bossGroup,只有工作现场
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    //开启TCP_NODELAY 意味着禁用Nagle算法，TCP将不会缓存，
                    // 在小数据包大量通信又不能延时的时候可能会用到
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new SimpleChannelInitializer());

            // 发起异步连接操作
            ChannelFuture f = b.connect(host, port).sync();

            // 等待客户端链路关闭
            //f.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放线程池资源
            //group.shutdownGracefully();
        }
    }


    private class SimpleChannelInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new ConnentHandler()).addLast(new StickyDemoClientHandler());
        }
    }
}
