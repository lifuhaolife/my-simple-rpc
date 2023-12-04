package com.lfh.rpc.server.transport.netty;

import com.lfh.rpc.server.transport.protocol.Command;
import com.lfh.rpc.server.transport.protocol.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/3 16:52
 */
public abstract class CommandEncoder extends MessageToByteEncoder {


    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf byteBuf) throws Exception {
        if (!(msg instanceof Command)) {
            throw new Exception(String.format("Unknown type: %s!", msg.getClass().getCanonicalName()));
        }
        Command command = (Command) msg;
        byteBuf.writeInt(Integer.BYTES + command.getHeader().length() + command.getPayload().length);
        encodeHeader(ctx, command.getHeader(), byteBuf);
        byteBuf.writeBytes(command.getPayload());

    }

    protected void encodeHeader(ChannelHandlerContext ctx, Header header, ByteBuf byteBuf) {
        byte[] requestIdBytes = header.getRequestId().getBytes(StandardCharsets.UTF_8);
        byteBuf.writeInt(requestIdBytes.length);
        byteBuf.writeBytes(requestIdBytes);
        byte[] versionBytes = header.getVersion().getBytes(StandardCharsets.UTF_8);
        byteBuf.writeInt(versionBytes.length);
        byteBuf.writeBytes(versionBytes);
        byteBuf.writeInt(header.getType());
    }
}
