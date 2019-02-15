package io.netty.mitix.rpc.v3;

import io.netty.mitix.rpc.v3.common.RpcRequest;
import io.netty.mitix.rpc.v3.common.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.SynchronousQueue;

/**
 * @author oldflame-jm
 * @create 2018/12
 * @since
 */
public class RpcInvocationHandler implements InvocationHandler {
    private NettyClient client;
    private Class<?> proxyClass;

    public RpcInvocationHandler(Class<?> proxyClass, NettyClient client) {
        this.proxyClass = proxyClass;
        this.client = client;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        Class<?>[] paramTypes = method.getParameterTypes();
        if ("toString".equals(methodName) && paramTypes.length == 0) {
            return proxy.toString();
        } else if ("hashCode".equals(methodName) && paramTypes.length == 0) {
            return proxy.hashCode();
        } else if ("equals".equals(methodName) && paramTypes.length == 1) {
            Object another = args[0];
            return proxy == another || proxy.getClass().isInstance(another);
        }

        RpcRequest request = buildRequest(proxyClass.getName(), method, args);
        RpcResponse response = sendRPCRequest(request);

        return response.getResult();
    }

    private RpcResponse sendRPCRequest(RpcRequest request) {
        SynchronousQueue<RpcResponse> queue = new SynchronousQueue<>();
        NettyClient.putSunchronousQuee(request.getId(), queue);
        RpcResponse response = null;
        try {
            client.sendRpcRequest(request);
            response = queue.take();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return response;
    }

    public static NettyClient parseInvoker(Object proxyObject) {
        InvocationHandler handler = java.lang.reflect.Proxy.getInvocationHandler(proxyObject);
        if (handler instanceof RpcInvocationHandler) {
            return ((RpcInvocationHandler) handler).getProxyInvoker();
        }
        return null;
    }

    public Class<?> getProxyClass() {
        return proxyClass;
    }

    public NettyClient getProxyInvoker() {
        return client;
    }

    private RpcRequest buildRequest(String serviceName, Method method, Object[] args) {

        String id = UUID.randomUUID().toString();

        RpcRequest request = new RpcRequest();
        request.setServiceName(serviceName);

        request.setId(id);
        request.setMethodName(method.getName());
        request.setArgs(args);

        List<String> parameterTypeNames = new ArrayList<String>();
        for (Class<?> parameterType : method.getParameterTypes()) {
            parameterTypeNames.add(parameterType.getName());
        }

        request.setParameterTypeNames(parameterTypeNames.toArray(new String[0]));

        return request;
    }

}
