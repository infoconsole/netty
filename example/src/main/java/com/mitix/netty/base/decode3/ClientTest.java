package com.mitix.netty.base.decode3;

import io.netty.util.internal.StringUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

/**
 * @author oldflame-jm
 * @create 2018/10
 * @since
 */
public class ClientTest {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 9999);
        OutputStream outputStream = socket.getOutputStream();
        //车机序号
        outputStream.write(getBytesFixedlength("AC1235", "US-ASCII", 6));
        //控制字符
        outputStream.write(getBytesFixedlength("A", "", 1));
        //数据-状态
        outputStream.write(getBytesFixedlength("1", "", 1));
        //车辆载重状态
        outputStream.write(getBytesFixedlength("2", "", 1));
        //定位状态
        outputStream.write(getBytesFixedlength("V", "", 1));
        //日期ascii
        outputStream.write(getBytesFixedlength("181022", "US-ASCII", 6));
        //时间ascii
        outputStream.write(getBytesFixedlength("221526", "US-ASCII", 6));
        //纬度
        outputStream.write(getBytesFixedlength("3037.8333", "", 9));
        //经度
        outputStream.write(getBytesFixedlength("11944.6667", "", 10));
        //公里数
        //Todo  公里数格式不确定
        outputStream.write(getBytesFixedlength("30", "", 5));
        //方向数据
        outputStream.write(getBytesFixedlength("128.0", "", 5));
        //卫星数量BCD码
        outputStream.write(BCDConverter.str2Bcd("2", 2));
        //高架地面
        outputStream.write(getBytesFixedlength("1", "", 1));
        //刹车状态

        outputStream.write(getBytesFixedlength("1", "", 1));
        //gps速度BCD
        outputStream.write(BCDConverter.str2Bcd("66", 3));
        //校验码ascii
        outputStream.write(getBytesFixedlength("AC", "US-ASCII", 2));

        outputStream.close();
        socket.close();
    }

    /**
     * 处理定长
     *
     * @param source
     * @param charsetName
     * @param len
     * @return
     * @throws UnsupportedEncodingException
     */
    private static byte[] getBytesFixedlength(String source, String charsetName, int len) throws UnsupportedEncodingException {
        byte[] src;
        if (StringUtil.isNullOrEmpty(charsetName)) {
            src = source.getBytes();
        } else {
            src = source.getBytes(charsetName);
        }
        int lenReal = src.length < len ? src.length : len;
        byte[] det = new byte[lenReal];
        System.arraycopy(src, 0, det, 0, src.length < len ? src.length : len);
        byte[] dets = doByteFixed(det);


        return dets;
    }

    private static byte[] doByteFixed(byte[] det) {
        for (byte b : det) {
            switch (b) {
                case 0x01:
                case 0x02:
                case 0x03:
                case 0x04:
            }
        }
        return det;
    }
}
