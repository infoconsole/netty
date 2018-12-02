package com.mitix.chapter_03.example_02;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @author oldflame-jm
 * @create 2018/11
 * @since
 */
public class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        System.out.println("initChannel ...");
        socketChannel.pipeline().addLast(new TimeServerHandler());
    }
}
