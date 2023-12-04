package com.lfh.rpc.server.client;

import com.lfh.rpc.server.seriaize.SerializeSupport;
import com.lfh.rpc.server.transport.protocol.request.RpcRequest;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/4 21:48
 */
public class StubInvocationHandler extends AbstractStub implements InvocationHandler {

    private final String interfaceName;

    public StubInvocationHandler(String interfaceName) {
        this.interfaceName = interfaceName;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        byte[] bytes = invokeRemote(new RpcRequest(interfaceName, method.getName(), SerializeSupport.serialize(args[0])));
        return SerializeSupport.parse(bytes);
    }
}
