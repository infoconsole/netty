package io.netty.mitix.rpc.v2;


import io.netty.mitix.rpc.v2.common.CalculatorService;
import io.netty.mitix.rpc.v2.common.CalculatorServiceImpl;

public class RpcDemoServer {

    public static void main(String[] args) {

        RpcBuilder builder = new RpcBuilder();
        builder.publishService(CalculatorService.class, new CalculatorServiceImpl());

        NettyServer server = new NettyServer(8080);
        try {
            server.start(builder);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
