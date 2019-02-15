package com.mitix.netty.dcoder.lengthfield;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置一个定长处理的解码器
 */
public class StickyLengthFieldDecoder extends ChannelInboundHandlerAdapter {
    private static int MSG_LENGTH = 139;
    private ByteBuf cache = Unpooled.buffer(10000);


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        cache.writeBytes(in);

        List<ByteBuf> out = new ArrayList<>();
        while (cache.readableBytes() >= MSG_LENGTH) {
            out.add(cache.readBytes(MSG_LENGTH));
        }
        cache.discardReadBytes();

        for (ByteBuf buf : out) {
            ctx.fireChannelRead(buf);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
