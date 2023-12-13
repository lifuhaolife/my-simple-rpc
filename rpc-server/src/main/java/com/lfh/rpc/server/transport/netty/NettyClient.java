package com.lfh.rpc.server.transport.netty;

import com.lfh.rpc.server.transport.InFlightRequests;
import com.lfh.rpc.server.transport.NettyTransport;
import com.lfh.rpc.server.transport.Transport;
import com.lfh.rpc.server.transport.TransportClient;
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
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/11/30 23:57
 */
public class NettyClient implements TransportClient {

    private final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    private EventLoopGroup eventLoopGroup;
    private Bootstrap bootstrap;
    private List<Channel> channels = new ArrayList<>();
    private InFlightRequests inFlightRequests;

    public NettyClient() {
        inFlightRequests = new InFlightRequests();
    }

    private ChannelHandler newChannelHandler() {
        return new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline()
                        .addLast(new RequestEncoder())
                        .addLast(new ResponseDecoder())
                        .addLast(new ClientRequestHandler(inFlightRequests));
            }
        };
    }

    private Bootstrap newBootstrap(ChannelHandler channelHandler, EventLoopGroup eventLoopGroup) {
        bootstrap = new Bootstrap();
        return bootstrap.channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class).group(eventLoopGroup).handler(channelHandler).option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
    }

    public Channel connect(InetSocketAddress address, long connectTimeout) throws InterruptedException, TimeoutException {
        ChannelHandler channelHandler = newChannelHandler();
        eventLoopGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        Bootstrap bootstrap = newBootstrap(channelHandler, eventLoopGroup);
        ChannelFuture channelFuture;
        channelFuture = bootstrap.connect(address);
        if (!channelFuture.await(connectTimeout, TimeUnit.SECONDS)) {
            throw new TimeoutException();
        }
        Channel channel = channelFuture.channel();
        if (channel == null || !channel.isActive()) {
            throw new IllegalStateException();
        }
        channels.add(channel);
        logger.info("netty connect socket address :{}, port : {} success", address.getAddress(), address.getPort());
        return channel;
    }

    @Override
    public Transport createTransport(InetSocketAddress address, long connectTimeout) throws InterruptedException, TimeoutException {
        Channel channel = connect(address, connectTimeout);
        return new NettyTransport(channel, inFlightRequests);
    }

    @Override
    public void close() throws IOException {
        if (eventLoopGroup != null) {
            eventLoopGroup.shutdownGracefully();
        }
        for (Channel channel : channels) {
            if (channel != null) {
                channel.close();
            }
        }
    }
}
