package com.mitix.netty.base.netty4subbind.good;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author oldflame-jm
 * @create 2018/6/9
 * 基于netty的timeserver
 */
public class TimeServer4NettySubBind {
    private int port = 8080;

    public static void main(String[] args) throws InterruptedException {
        TimeServer4NettySubBind timeServer = new TimeServer4NettySubBind();
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
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChildChannelHandler4NettySubBind());
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
