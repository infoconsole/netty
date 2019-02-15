package com.mitix.netty.base.decode3;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author oldflame-jm
 * @create 2018/6/6
 * ${DESCRIPTION}
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    int counter = 0;


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object data) throws Exception {
        ByteBuf buffer = (ByteBuf) data;
        byte[] req = new byte[buffer.readableBytes()];
        buffer.readBytes(req);
        StringBuffer sb = new StringBuffer();
        //车机序号
        sb.append(new String(req, 0, 6, "US-ASCII"));
        //控制字符
        sb.append(new String(req, 6, 1));
        //数据-状态
        sb.append(new String(req, 7, 1));
        //车辆载重状态
        sb.append(new String(req, 8, 1));
        //定位状态
        sb.append(new String(req, 9, 1));
        //日期ascii
        sb.append(new String(req, 10, 6, "US-ASCII"));
        //时间ascii
        sb.append(new String(req, 16, 6, "US-ASCII"));
        //纬度
        sb.append(new String(req, 22, 9));
        //经度
        sb.append(new String(req, 31, 10));
        //公里数
        //Todo  公里数格式不确定
        sb.append(new String(req, 41, 5));
        //方向数据
        sb.append(new String(req, 46, 5));
        //卫星数量BCD码
        byte[] satellite = new byte[2];
        System.arraycopy(req, 51, satellite, 0, 2);
        sb.append(BCDConverter.bcd2Str(satellite,2));
        //高架地面
        sb.append(new String(req, 53, 1));
        //刹车状态
        sb.append(new String(req, 54, 1));
        //gps速度BCD
        byte[] gpsSpeed = new byte[3];
        System.arraycopy(req, 55, gpsSpeed, 0, 3);
        sb.append(BCDConverter.bcd2Str(gpsSpeed,3));
        //校验码ascii
        sb.append(new String(req, 58, 2, "US-ASCII"));
        System.out.println("接收客户端数据:" + sb.toString());
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
