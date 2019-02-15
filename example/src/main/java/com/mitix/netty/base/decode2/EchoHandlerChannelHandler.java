package com.mitix.netty.base.decode2;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;

/**
 * @author oldflame-jm
 * @create 2018/12
 * @since
 */
public class EchoHandlerChannelHandler extends ChannelHandlerAdapter {
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        ChannelPipeline pipeline = ctx.pipeline();
        System.out.println(pipeline.toString());
    }
}
