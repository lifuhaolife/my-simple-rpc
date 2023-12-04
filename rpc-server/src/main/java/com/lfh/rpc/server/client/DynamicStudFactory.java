package com.lfh.rpc.server.client;

import com.lfh.rpc.server.transport.Transport;
import java.lang.reflect.Proxy;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/4 21:52
 */
public class DynamicStudFactory implements StubFactory {
    @Override
    public <T> T create(Transport transport, Class<T> tClass) {
        StubInvocationHandler stubInvocationHandler = new StubInvocationHandler(tClass.getCanonicalName());
        stubInvocationHandler.setTransport(transport);
        return (T) Proxy.newProxyInstance(tClass.getClassLoader(),
                new Class[]{tClass},
                stubInvocationHandler);
    }
}
