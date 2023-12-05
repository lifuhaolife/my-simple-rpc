package com.lfh.rpc.server.transport.netty;

import com.alibaba.fastjson.JSON;
import com.lfh.rpc.server.transport.InFlightRequests;
import com.lfh.rpc.server.transport.protocol.Command;
import com.lfh.rpc.server.transport.protocol.response.ResponseFuture;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/1 21:42
 */
public class ClientRequestHandler extends SimpleChannelInboundHandler<Command> {

    private final Logger logger = LoggerFactory.getLogger(ClientRequestHandler.class);


    private final InFlightRequests inFlightRequests;

    public ClientRequestHandler(InFlightRequests inFlightRequests) {
        this.inFlightRequests = inFlightRequests;
    }

    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        boolean b = super.acceptInboundMessage(msg);
        logger.info("inbound message {}  ans state is {}", JSON.toJSONString(msg), b);
        return b;
    }


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("channel registered");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("channel unregistered");
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("channel active");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("channel inactive");
        super.channelInactive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.info("channel read complete");
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        logger.info("user event triggered");
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        logger.info("channel writability changed");
        super.channelWritabilityChanged(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Command response) {
        //reply 消息发送通过completable 转换完成
        ResponseFuture responseFuture = inFlightRequests.remove(response.getHeader().getRequestId());
        if (null != responseFuture) {
            responseFuture.getFuture().complete(response);
        } else {
            logger.warn("Drop  response :  {}", response);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("has a exception ", cause);
        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        if (channel.isActive()) {
            channel.close();
        }
    }

}
