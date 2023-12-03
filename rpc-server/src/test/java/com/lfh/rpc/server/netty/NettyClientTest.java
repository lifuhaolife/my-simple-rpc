package com.lfh.rpc.server.netty;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.lfh.rpc.server.transport.protocol.Command;
import com.lfh.rpc.server.transport.protocol.Header;
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


    private static final Snowflake snowflake = IdUtil.getSnowflake();


    /**
     * 发送消息格式按照：
     *
     * @param args
     * @throws RuntimeException
     */
    public static void main(String[] args) throws RuntimeException {
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
                    String requestId = String.valueOf(snowflake.nextId());
                    Command command = new Command(new Header("1.0.0", requestId, 1), s.getBytes(StandardCharsets.UTF_8));
                    ChannelFuture channelFuture = channel.writeAndFlush(command);
                    channelFuture.addListener(future -> {
                        if (future.isSuccess()) {
                            System.out.println("send messages success");
                        } else {
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