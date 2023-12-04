package com.lfh.rpc.server.transport.netty;

import com.lfh.rpc.server.transport.protocol.Header;
import com.lfh.rpc.server.transport.protocol.ResponseHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.nio.charset.StandardCharsets;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/1 20:57
 */
public class ResponseDecoder extends CommandDecoder {

    @Override
    protected Header decodeHeader(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        int requestIdLength = byteBuf.readInt();
        byte[] requestBytes = new byte[requestIdLength];
        byteBuf.readBytes(requestBytes);
        int versionLength = byteBuf.readInt();
        byte[] versionBytes = new byte[versionLength];
        byteBuf.readBytes(versionBytes);
        int type = byteBuf.readInt();
        String version = new String(versionBytes, StandardCharsets.UTF_8);
        String requestId = new String(requestBytes, StandardCharsets.UTF_8);
        ResponseHeader responseHeader =
                new ResponseHeader(version, requestId, type);
        int code = byteBuf.readInt();
        responseHeader.setCode(code);
        int messageLength = byteBuf.readInt();
        if (messageLength > 0) {
            byte[] bytes = new byte[messageLength];
            byteBuf.readBytes(bytes);
            responseHeader.setErrMsg(new String(bytes, StandardCharsets.UTF_8));
        }
        return responseHeader;
    }


}
