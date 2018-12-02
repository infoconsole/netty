package com.mitix.chapter_03.example_02;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author oldflame-jm
 * @create 2018/11
 * @since
 */
public class TimeServer {
    private int port = 8080;

    public static void main(String[] args) throws InterruptedException {
        TimeServer timeServer = new TimeServer();
        timeServer.bind();
    }

    public void bind() throws InterruptedException {
        //配置nio线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .option(ChannelOption.SO_BACKLOG, 1024).childHandler(new ChildChannelHandler());
            //绑定端口，同步等待成功
            ChannelFuture future = bootstrap.bind().sync();
            //等待服务端监听端口关闭
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
