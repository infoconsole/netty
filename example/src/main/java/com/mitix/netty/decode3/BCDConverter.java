package com.mitix.netty.decode3;

/**
 * @author oldflame-jm
 * @create 2018/10
 * @since
 */
public class BCDConverter {
    /**
     * @功能: BCD码转为10进制串(阿拉伯数据)
     * @参数: BCD码
     * @结果: 10进制串
     */
    public static String bcd2Str(byte[] data, int strlens) {
        int lens = 0;
        for (byte t : data) {
            if (t == 0) {
                break;
            }
            lens++;
        }
        byte[] real = new byte[lens];
        System.arraycopy(data, 0, real, 0, lens);
        StringBuffer temp = new StringBuffer(real.length * 2);
        for (int i = 0; i < real.length; i++) {
            temp.append((byte) ((real[i] & 0xf0) >>> 4));
            temp.append((byte) (real[i] & 0x0f));
        }
        String detstr = temp.substring(0, 1).equalsIgnoreCase("0") ? temp.substring(1) : temp.substring(0);
        for (; detstr.length() < strlens; ) {
            detstr = " " + detstr;
        }
        return detstr;
    }

    /**
     * @功能: 10进制串转为BCD码
     * @参数: 10进制串
     * @结果: BCD码
     */
    public static byte[] str2Bcd(String asc, int rlen) {
        int len = asc.length();
        int mod = len % 2;
        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }
        byte abt[] = new byte[len];
        if (len >= 2) {
            len = len / 2;
        }
        byte bbt[] = new byte[len];
        abt = asc.getBytes();
        int j, k;
        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }
            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }
            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }

        byte[] det = new byte[rlen];
        System.arraycopy(bbt, 0, det, 0, bbt.length < rlen ? bbt.length : rlen);
        return det;
    }
}