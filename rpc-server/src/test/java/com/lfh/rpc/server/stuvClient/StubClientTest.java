package com.lfh.rpc.server.stuvClient;

import com.lfh.rpc.server.client.DynamicStudFactory;
import com.lfh.rpc.server.stuvClient.test.Hello;
import com.lfh.rpc.server.transport.InFlightRequests;
import com.lfh.rpc.server.transport.NettyTransport;
import com.lfh.rpc.server.transport.netty.NettyClient;
import io.netty.channel.Channel;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/4 23:56
 */
public class StubClientTest {

    private static final Logger logger = LoggerFactory.getLogger(StubClientTest.class);


    @Test
    public void testDynamicProxy() {
        InvocationHandler invocationHandler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("hello");
                return "null";
            }
        };
        Object o = Proxy.newProxyInstance(Hello.class.getClassLoader(), new Class[]{Hello.class}, invocationHandler);
        Hello hello1 = (Hello) o;
        System.out.println(hello1.sayHello("world"));
    }


    @Test
    public void testClassName() {
        DynamicStudFactory dynamicStudFactory = new DynamicStudFactory();
        Hello hello = dynamicStudFactory.create(null, Hello.class);

    }

    public static void main(String[] args) {
        try (InFlightRequests inFlightRequests = new InFlightRequests();
             NettyClient client = new NettyClient(new InetSocketAddress("localhost", 8099), 10);) {
            client.setInFlightRequests(inFlightRequests);
            client.connect();
            //连接一个通道
            Channel channel = client.getChannel();
            NettyTransport nettyTransport = new NettyTransport(channel, inFlightRequests);
            //创建桩：
            DynamicStudFactory dynamicStudFactory = new DynamicStudFactory();
            Hello hello = dynamicStudFactory.create(nettyTransport, Hello.class);

            Scanner scanner = new Scanner(System.in);
            while (true) {
                String msg = scanner.nextLine();
                if (StringUtils.isNotBlank(msg)) {
                    if ("exit".equals(msg)) {
                        throw new IllegalStateException();
                    }
                    String result = hello.sayHello(msg);
                    System.out.println(result);
                    logger.info("RPC result is : {}", result);
                }
            }
        } catch (IOException | InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
