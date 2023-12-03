package com.lfh.rpc.server.netty;

import com.lfh.rpc.server.transport.protocol.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.nio.charset.StandardCharsets;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/1 20:55
 */
public class RequestDecoder extends CommandDecoder {

    @Override
    protected Header decodeHeader(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        int requestIdLength = byteBuf.readInt();
        byte[] requestBytes = new byte[requestIdLength];
        byteBuf.readBytes(requestBytes);
        int versionLength = byteBuf.readInt();
        byte[] versionBytes = new byte[versionLength];
        byteBuf.readBytes(versionBytes);
        return new Header(new String(versionBytes, StandardCharsets.UTF_8),
                new String(requestBytes, StandardCharsets.UTF_8),
                byteBuf.readInt());
    }

}
