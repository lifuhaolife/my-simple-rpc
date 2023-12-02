package com.lfh.rpc.server.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
import org.junit.platform.commons.util.StringUtils;


/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/1 21:37
 */
class NettyClientTest {


    public static void main(String[] args) throws RuntimeException{
        try (NettyClient client = new NettyClient(new InetSocketAddress("localhost", 8099), 10);) {
            client.connect();
            Channel channel = client.getChannel();
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String s = scanner.nextLine();
                if (StringUtils.isNotBlank(s)) {
                    if ("exit".equals(s)) {
                        throw new IllegalStateException();
                    }
                    ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(s.getBytes(StandardCharsets.UTF_8).length);
                    buffer.writeBytes(s.getBytes(StandardCharsets.UTF_8));
                    ChannelFuture channelFuture = channel.writeAndFlush(buffer);
                    channelFuture.addListener( future ->  {
                        if (future.isSuccess()) {
                            System.out.println("send messages success");
                        }else {
                            System.out.println("send message error");
                            future.cause().printStackTrace();
                        }
                    });
                }


            }
        } catch (IOException | InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}