package com.lfh.rpc.server;

import com.lfh.rpc.api.RpcAccessPoint;
import com.lfh.rpc.api.common.ServiceLoaderSupport;
import com.lfh.rpc.server.client.StubFactory;
import com.lfh.rpc.server.service.RequestHandlerDispatch;
import com.lfh.rpc.server.service.ServiceProviderRegistry;
import com.lfh.rpc.server.transport.Transport;
import com.lfh.rpc.server.transport.TransportClient;
import com.lfh.rpc.server.transport.TransportServer;
import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/7 23:15
 */
public class NettyRpcAccessPoint implements RpcAccessPoint {

    private final String host = "localhost";
    private final int port = 9999;
    private final URI uri = URI.create("rpc://" + host + ":" + port);

    private TransportServer server;
    private Map<URI, Transport> clientMap = new ConcurrentHashMap<>();

    private TransportClient client = ServiceLoaderSupport.serviceLoader(TransportClient.class);
    private StubFactory stubFactory = ServiceLoaderSupport.serviceLoader(StubFactory.class);

    private ServiceProviderRegistry serviceProviderRegistry = ServiceProviderRegistry.getInstance();
    @Override
    public <T> T getRemoteService(URI uri, Class<T> serviceClass) {
        // 创建连接：  获取桩对象： 连接客户端，
        Transport transport = clientMap.computeIfAbsent(uri, this::createTransport);
        return stubFactory.create(transport, serviceClass);
    }

    private Transport createTransport(URI uri) {
        try {
            return client.createTransport(new InetSocketAddress(uri.getHost(), uri.getPort()), 3000L);
        } catch (InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> URI addServiceProvider(T service, Class<T> serviceClass) {
        serviceProviderRegistry.addServiceProviders(serviceClass, service);
        return uri;
    }

    @Override
    public synchronized Closeable startServer() throws Exception {
        if (null == server) {
            server = ServiceLoaderSupport.serviceLoader(TransportServer.class);
            server.start(RequestHandlerDispatch.getInstance(), port);
        }

        return () -> {
            if (null != server) {
                server.stop();
            }
        };
    }

    @Override
    public void close() throws IOException {
        if (null != server) {
            server.stop();
        }
    }
}
