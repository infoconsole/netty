package com.mitix.netty.base.decode1;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author oldflame-jm
 * @create 2018/6/6
 * ${DESCRIPTION}
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {
    private int counter;
    static final String ECHO_REQ = "Hi,oldflame-Jm. welcome to Netty.$_";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        System.out.println("This is " + ++counter + "time receive client :[" + body + "]");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 20; i++) {
            ctx.writeAndFlush(Unpooled.copiedBuffer(Unpooled.copiedBuffer(ECHO_REQ.getBytes())));
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
