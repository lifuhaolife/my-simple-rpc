package com.lfh.rpc.server.transport.netty;

import com.lfh.rpc.server.service.RequestHandlerDispatch;
import com.lfh.rpc.server.transport.TransportServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/11/29 22:57
 */
public class NettyServer implements TransportServer {

    private final Logger logger = LoggerFactory.getLogger(NettyServer.class);
    private int port;
    private EventLoopGroup acceptEventGroup;
    private EventLoopGroup ioEventGroup;
    private Channel channel;
    private RequestHandlerDispatch requestHandlerDispatch;

    private Channel doBind(ServerBootstrap serverBootstrap) throws InterruptedException {
        return serverBootstrap.bind(port)
                .sync()
                .channel();
    }

    private ChannelHandler newChannelHandlerPipeline() {
        return new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline()
                        .addLast(new RequestDecoder())
                        .addLast(new ResponseEncoder())
                        .addLast(new ServerResponseHandler(requestHandlerDispatch));

            }
        };
    }

    private EventLoopGroup newEventLoopGroup() {
        if (Epoll.isAvailable()) {
            return new EpollEventLoopGroup();
        }
        return new NioEventLoopGroup();
    }

    @Override
    public void start(RequestHandlerDispatch requestHandlerDispatch, int port) throws Exception {
        this.requestHandlerDispatch = requestHandlerDispatch;
        this.port = port;
        this.acceptEventGroup = newEventLoopGroup();
        this.ioEventGroup = newEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap()
                .group(acceptEventGroup, ioEventGroup);
        ChannelHandler channelHandler = newChannelHandlerPipeline();
        serverBootstrap.channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(channelHandler);

        channel = doBind(serverBootstrap);
        logger.info(" localhost:{} netty server start success", port);
    }

    @Override
    public void stop() {
        if (acceptEventGroup != null) {
            acceptEventGroup.shutdownGracefully();
        }
        if (ioEventGroup != null) {
            ioEventGroup.shutdownGracefully();
        }
        if (channel != null) {
            channel.close();
        }
    }

}
