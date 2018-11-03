package com.mitix.javaser;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * @author oldflame-jm
 * @create 2018/6/12
 * ${DESCRIPTION}
 */
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 7899329213458307059L;

    private String userName;

    private int userId;

    public UserInfo(String userName, Integer userId) {
        this.userId = userId;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public byte[] codeC() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byte[] value = this.getUserName().getBytes();
        byteBuffer.putInt(value.length);
        byteBuffer.put(value);
        byteBuffer.putInt(this.userId);
        byteBuffer.flip();
        value = null;
        byte[] result = new byte[byteBuffer.remaining()];
        byteBuffer.get(result);
        return result;
    }
}
