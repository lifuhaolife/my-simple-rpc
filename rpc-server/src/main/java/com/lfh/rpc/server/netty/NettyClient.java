package com.lfh.rpc.server.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/11/30 23:57
 */
public class NettyClient  implements Closeable {

    private final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    private EventLoopGroup eventLoopGroup;
    private Bootstrap bootstrap;

    private Channel channel;

    private InetSocketAddress socketAddress;

    private long connectTimeout;

    public NettyClient(InetSocketAddress socketAddress, long connectTimeout) {
        this.socketAddress = socketAddress;
        this.connectTimeout = connectTimeout;

    }

    public Channel getChannel() {
        return channel;
    }

    private ChannelHandler newChannelHandler() {
        return new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline()
                        .addLast(new RequestEncoder())
                        .addLast(new ResponseDecoder())
                        .addLast( new ClientRequestHandler());
            }
        };
    }

    private Bootstrap newBootstrap(ChannelHandler channelHandler, EventLoopGroup eventLoopGroup) {
        bootstrap = new Bootstrap();
        return bootstrap.channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class).group(eventLoopGroup).handler(channelHandler).option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
    }

    public void connect() throws InterruptedException, TimeoutException {
        ChannelHandler channelHandler = newChannelHandler();
        eventLoopGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        Bootstrap bootstrap = newBootstrap(channelHandler, eventLoopGroup);
        ChannelFuture channelFuture;
        channelFuture = bootstrap.connect(socketAddress);
        if (!channelFuture.await(connectTimeout, TimeUnit.SECONDS)) {
            throw new TimeoutException();
        }
        channel = channelFuture.channel();
        if (channel == null || !channel.isActive()) {
            throw new IllegalStateException();
        }
        logger.info("netty connect socket address :{}, port : {} success", socketAddress.getAddress(), socketAddress.getPort());
    }

    @Override
    public void close() throws IOException {
        if (eventLoopGroup != null) {
            eventLoopGroup.shutdownGracefully();
        }
        if (channel != null) {
            channel.close();
        }
    }
}
