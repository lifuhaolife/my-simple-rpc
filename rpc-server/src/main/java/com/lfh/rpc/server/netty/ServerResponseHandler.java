package com.lfh.rpc.server.netty;

import com.alibaba.fastjson.JSON;
import com.lfh.rpc.server.transport.protocol.Command;
import com.lfh.rpc.server.transport.protocol.Header;
import com.lfh.rpc.server.transport.protocol.ResponseHeader;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/1 21:35
 */
public class ServerResponseHandler extends SimpleChannelInboundHandler<Command> {

    private Logger logger = LoggerFactory.getLogger(ServerResponseHandler.class);


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
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("caught exception ", cause);
        super.exceptionCaught(ctx, cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Command msg) {
        //读取消息处理
        logger.info("receive message :{}", msg);
        //完成消息返回
        Header header = msg.getHeader();
        ResponseHeader responseHeader = new ResponseHeader(header.getVersion(), header.getRequestId(), header.getType());
        Command command = new Command(responseHeader, "server echo success".getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(command);


    }
}
