package io.netty.mitix.rpc.v3;


import io.netty.mitix.rpc.v3.common.CalculatorService;
import io.netty.mitix.rpc.v3.common.CalculatorServiceImpl;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;

import java.net.UnknownHostException;

public class RpcDemoServer {

    public static void main(String[] args) throws UnknownHostException {
        int port = 8080;
        NettyServer server = new NettyServer(port);
        ZkClient zkClient = new ZkClient(new ZkConnection("127.0.0.1:2181"), 10000);
        RpcBuilder builder = new RpcBuilder(zkClient, port);
        builder.publishService(CalculatorService.class, new CalculatorServiceImpl());


        try {
            server.start(builder);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
