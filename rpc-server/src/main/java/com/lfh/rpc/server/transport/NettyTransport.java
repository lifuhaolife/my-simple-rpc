package com.lfh.rpc.server.transport;

import com.lfh.rpc.server.transport.protocol.Command;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import java.util.concurrent.CompletableFuture;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/4 22:21
 */
public class NettyTransport implements Transport {

    private final Channel channel;

    public NettyTransport(Channel channel) {
        this.channel = channel;
    }

    @Override
    public CompletableFuture<Command> send(Command command) {

        CompletableFuture<Command> commandFuture = new CompletableFuture<>();

        try {
            // 发送命令
            channel.writeAndFlush(command).addListener((ChannelFutureListener) channelFuture -> {

                if (!channelFuture.isSuccess()) {
                    commandFuture.completeExceptionally(channelFuture.cause());
                    channel.close();
                }
            });
        } catch (Throwable throwable) {
            commandFuture.completeExceptionally(throwable);
        }

        return commandFuture;
    }
}
