package io.netty.mitix.rpc.v3;


import io.netty.mitix.rpc.v3.common.ClassUtils;
import io.netty.mitix.rpc.v3.common.RpcException;
import io.netty.mitix.rpc.v3.common.RpcRequest;
import io.netty.mitix.rpc.v3.common.RpcResponse;
import org.I0Itec.zkclient.ZkClient;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class RpcBuilder {
    private ZkClient zkClient;
    private ServerInfo serverInfo;
    private InetAddress address;
    private Map<String, Object> services = new ConcurrentHashMap<>();
    private Map<String, Class> interfaces = new ConcurrentHashMap<>();

    public RpcBuilder(ZkClient zkClient, int port) throws UnknownHostException {
        this.zkClient = zkClient;
        this.address = InetAddress.getLocalHost();
        serverInfo = new ServerInfo();
        serverInfo.setHost(address.getHostAddress());
        serverInfo.setPort(String.valueOf(port));
    }

    public void publishService(Class serviceInterface, Object service) {
        if (services.containsKey(serviceInterface.getName()) || interfaces.containsKey(serviceInterface.getName())) {
            throw new RpcException("serviceInterface has been already registered......");
        }

        if (!(serviceInterface.isInstance(service))) {
            throw new RpcException("service object must implement the service Interface .......");
        }

        services.put(serviceInterface.getName(), service);
        interfaces.put(serviceInterface.getName(), serviceInterface);

        zkClient.createEphemeral("/mitix");

        for (Method method : serviceInterface.getMethods()) {
            if ("toString".equals(method.getName()) || "hashCode".equals(method.getName()) || "equals".equals(method.getName())) {
            } else {
                zkClient.createPersistent("/mitix/rpc/" + serviceInterface.getName() + "/" + method.getName(), serverInfo.toString());
            }
        }
    }

    public RpcResponse invokeService(RpcRequest rpcRequest) {
        String serviceName = rpcRequest.getServiceName(); //拿到接口名
        String methodName = rpcRequest.getMethodName(); //拿到方法名，add
        Class serviceInterface = interfaces.get(serviceName); //拿到具体的接口，CalculatorService

        Class<?>[] parameterTypes = new Class[rpcRequest.getParameterTypeNames().length];
        for (int i = 0; i < parameterTypes.length; i++) {
            try {
                parameterTypes[i] = ClassUtils.getClass(rpcRequest.getParameterTypeNames()[i]);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        Method m;
        Object result = null;
        try {
            m = serviceInterface.getMethod(methodName, parameterTypes);
            result = m.invoke(services.get(serviceName), rpcRequest.getArgs());

        } catch (Exception e) {
            e.printStackTrace();
        }

        RpcResponse response = new RpcResponse();
        response.setId(rpcRequest.getId());
        response.setServiceName(serviceName);
        response.setMethodName(methodName);
        response.setResult((double) result);


        return response;
    }

}
