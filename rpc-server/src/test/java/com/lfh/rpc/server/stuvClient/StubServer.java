package com.lfh.rpc.server.stuvClient;

import com.lfh.rpc.server.service.ServiceProviderRegistry;
import com.lfh.rpc.server.stuvClient.test.Hello;
import com.lfh.rpc.server.stuvClient.test.HelloService;
import com.lfh.rpc.server.transport.netty.NettyServer;
import java.io.IOException;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/4 23:56
 */
public class StubServer {


    public static void main(String[] args) {

        ServiceProviderRegistry serviceRegistry = ServiceProviderRegistry.getInstance();
        Hello hello = new HelloService();
        serviceRegistry.addServiceProviders(Hello.class, hello);
        // 提前将服务注册进去
        try (NettyServer nettyServer = new NettyServer(8099)) {
            nettyServer.startServer();

            System.in.read();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
