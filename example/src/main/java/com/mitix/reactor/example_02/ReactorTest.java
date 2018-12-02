package com.mitix.reactor.example_02;

import java.io.IOException;

/**
 * @author oldflame-jm
 * @create 2018/11
 * @since
 */
public class ReactorTest {
    public static void main(String[] args) throws IOException {
        Reactor reactor = new Reactor(8080);
        reactor.run();
    }
}
