package com.lfh.rpc.server.netty;

import com.lfh.rpc.server.transport.protocol.Header;
import com.lfh.rpc.server.transport.protocol.ResponseHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.nio.charset.StandardCharsets;
import org.apache.commons.lang3.StringUtils;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/1 20:55
 */
public class ResponseEncoder extends CommandEncoder {


    @Override
    protected void encodeHeader(ChannelHandlerContext ctx, Header header, ByteBuf byteBuf) {
        super.encodeHeader(ctx, header, byteBuf);
        if (header instanceof ResponseHeader) {
            ResponseHeader responseHeader = (ResponseHeader) header;
            byteBuf.writeInt(responseHeader.getCode());
            if (StringUtils.isBlank(responseHeader.getErrMsg())) {
                // 特殊处理如果不存在错误信息、则返回-1标志
                byteBuf.writeInt(-1);
                return;
            }
            byte[] bytes = responseHeader.getErrMsg().getBytes(StandardCharsets.UTF_8);
            byteBuf.writeInt(bytes.length);
            byteBuf.writeBytes(bytes);
        }
    }
}
