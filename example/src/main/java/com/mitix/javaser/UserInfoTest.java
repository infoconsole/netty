package com.mitix.javaser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @author oldflame-jm
 * @create 2018/6/12
 * ${DESCRIPTION}
 */
public class UserInfoTest {
    public static void main(String[] args) throws IOException {
        UserInfo userInfo = new UserInfo("hong.lvhang", 100);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(userInfo);
        os.flush();
        os.close();
        byte[] b = bos.toByteArray();
        System.out.println("the jdk serializable length is :" + b.length);
        bos.close();
        System.out.println("--------------------------------------------");
        System.out.println("the byte array serializable length is :" + userInfo.codeC().length);
    }
}
