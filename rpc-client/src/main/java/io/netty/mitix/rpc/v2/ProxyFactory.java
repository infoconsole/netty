package io.netty.mitix.rpc.v2;

import java.lang.reflect.InvocationHandler;

/**
 * @author oldflame-jm
 * @create 2018/12
 * @since
 */
public final class ProxyFactory {
    public static <T> T getProxy(Class<T> interfaceClass, NettyClient proxyInvoker) {
        InvocationHandler handler = new RpcInvocationHandler(interfaceClass, proxyInvoker);
        ClassLoader classLoader = ProxyFactory.class.getClassLoader();
        T result = (T) java.lang.reflect.Proxy.newProxyInstance(classLoader,
                new Class[] { interfaceClass }, handler);
        return result;
    }
}
