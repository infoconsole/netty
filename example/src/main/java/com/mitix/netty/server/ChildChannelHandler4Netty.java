package com.mitix.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @author oldflame-jm
 * @create 2018/6/9
 * ${DESCRIPTION}
 */
public class ChildChannelHandler4Netty extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        System.out.println("initChannel ...");
        socketChannel.pipeline().addLast(new TimeServerHandler4Netty());

    }
}
