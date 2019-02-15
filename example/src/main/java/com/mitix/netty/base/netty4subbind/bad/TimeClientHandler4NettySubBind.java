package com.mitix.netty.base.netty4subbind.bad;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * p
 *
 * @author oldflame-jm
 * @create 2018/6/9
 * ${DESCRIPTION}
 */
public class TimeClientHandler4NettySubBind extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        String req = "query time order";
        byte[] reqb = req.getBytes();
        ByteBuf byteBuf = null;
        for (int i = 0; i < 1000; i++) {
            byteBuf = Unpooled.buffer(reqb.length);
            byteBuf.writeBytes(reqb);
            ctx.writeAndFlush(byteBuf);
        }
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("Now is :" + msg.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

}
