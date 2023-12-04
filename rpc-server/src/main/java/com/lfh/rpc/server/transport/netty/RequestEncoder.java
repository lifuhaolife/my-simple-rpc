package com.lfh.rpc.server.transport.netty;

import com.lfh.rpc.server.transport.protocol.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/1 20:56
 */
public class RequestEncoder extends CommandEncoder {

    @Override
    protected void encodeHeader(ChannelHandlerContext ctx, Header header, ByteBuf byteBuf) {
        super.encodeHeader(ctx, header, byteBuf);
    }
}
