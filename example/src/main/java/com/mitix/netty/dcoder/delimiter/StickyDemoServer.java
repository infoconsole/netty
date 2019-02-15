package com.mitix.netty.dcoder.delimiter;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

public class StickyDemoServer {
    private static final int port = 8080;

    public static void main(String[] args) throws InterruptedException {
        new StickyDemoServer().start();
    }


    private void start() throws InterruptedException {
        // 第一步：
        // 配置服务端的NIO线程组
        // 主线程组, 用于接受客户端的连接，但是不做任何具体业务处理，像老板一样，负责接待客户，不具体服务客户
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // 工作线程组, 老板线程组会把任务丢给他，让手下线程组去做任务，服务客户
        EventLoopGroup workerGroup = new NioEventLoopGroup(4);

        try {
            // 类ServerBootstrap用于配置Server相关参数，并启动Server
            ServerBootstrap b = new ServerBootstrap();

            // 链式调用
            // 配置parentGroup和childGroup
            b.group(bossGroup, workerGroup)
                    // 配置Server通道
                    .channel(NioServerSocketChannel.class)
                    // 配置通道的ChannelPipeline
                    .childHandler(new ChildChannelHandler());

            // 绑定端口，并启动server，同时设置启动方式为同步
            ChannelFuture f = b.bind(port).sync();

            System.out.println(
                    StickyDemoServer.class.getName() + " 启动成功，在地址[" + f.channel().localAddress() + "]上等待客户请求......");

            // 等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully().sync();
            workerGroup.shutdownGracefully().sync();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            //使用解析器
            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(8192, Unpooled.wrappedBuffer(new byte[]{'#'})));
            //自定义的handler处理器
//            ch.pipeline().addLast("framer", new DelimiterBasedFrameDecoder(8192,
//                    Unpooled.wrappedBuffer(new byte[] { '#' })));
//            ch.pipeline().addLast("framer", new StickyDemoDecodeHandler(139));
            ch.pipeline().addLast(new StickyDemoServerHandler());

//            //ch.pipeline().addLast("framer", new FixedLengthFrameDecoder(139));
//            ch.pipeline().addLast("framer", new DelimiterBasedFrameDecoder(8192,
//                    Unpooled.wrappedBuffer(new byte[]{'#'})));
//            ch.pipeline().addLast("framer", new StickyDemoDecodeHandler(139));
//            // ch.pipeline().addLast("framer", new StickyDemoDecodeHandlerV2(
//            //		 Unpooled.wrappedBuffer(new byte[] { '#' })));

        }
    }
}
