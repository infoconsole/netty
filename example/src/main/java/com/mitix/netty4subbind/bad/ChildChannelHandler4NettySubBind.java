package com.mitix.netty4subbind.bad;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.CharsetUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author oldflame-jm
 * @create 2018/6/9
 * ${DESCRIPTION}
 */
public class ChildChannelHandler4NettySubBind extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        System.out.println("initChannel ...");
        socketChannel.pipeline().addLast(new TimeServerHandler4NettySubBind());

    }


    public class TimeServerHandler4NettySubBind extends ChannelInboundHandlerAdapter {
        private int count = 0;

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf in = (ByteBuf) msg;
            String body = in.toString(CharsetUtil.UTF_8);
            System.out.println("count is " + ++count + "  the time server receive order :" + body);
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            String currentTime = "query time order".equalsIgnoreCase(body.trim()) ? format.format(new Date()) : "bad order";
            ByteBuf out = Unpooled.copiedBuffer(currentTime.getBytes());
            ctx.write(out);
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
}
