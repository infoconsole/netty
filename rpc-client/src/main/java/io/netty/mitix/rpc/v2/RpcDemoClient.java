package io.netty.mitix.rpc.v2;

import io.netty.mitix.rpc.v2.common.CalculatorService;
import io.netty.mitix.rpc.v2.serialize.SerializerFactory;

public class RpcDemoClient {

    public static void main(String[] args) {

        NettyClient client = null;
        try {
            client = new NettyClient();
            client.init("127.0.0.1", 8080, SerializerFactory.getSerializer());
        } catch (Exception e) {
            client = null;
            e.printStackTrace();
        }

        CalculatorService service = ProxyFactory.getProxy(CalculatorService.class, client);

        double result = service.add(1.0, 2.0);

        System.out.println(result);

        client.close();

//        proxy.stop();
//
//        CalculatorServiceProxy proxy = new CalculatorServiceProxy("127.0.0.1", 8080);
//
//        System.out.println(proxy.add(1.0, 2.0));
//
//        proxy.stop();
    }

}
