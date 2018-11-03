package com.mitix.netty4subbind.good;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author oldflame-jm
 * @create 2018/6/9
 * ${DESCRIPTION}
 */
public class TimeClientHandler4NettySubBind extends ChannelInboundHandlerAdapter {
    private int counter;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        String req = "query time order" + System.getProperty("line.separator");
        byte[] reqb = req.getBytes();
        ByteBuf message = null;
        for (int i = 0; i < 1000; i++) {
            message = Unpooled.buffer(reqb.length);
            message.writeBytes(reqb);
            ctx.writeAndFlush(message);
        }
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        String body = (String) msg;
        System.out.println("Now is : " + body + " ; the counter is : "
                + ++counter);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

}
