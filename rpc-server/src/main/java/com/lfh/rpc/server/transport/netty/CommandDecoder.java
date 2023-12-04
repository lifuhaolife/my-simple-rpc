package com.lfh.rpc.server.transport.netty;

import com.lfh.rpc.server.transport.protocol.Command;
import com.lfh.rpc.server.transport.protocol.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/3 17:21
 */
public abstract class CommandDecoder extends ByteToMessageDecoder {

    private Logger logger = LoggerFactory.getLogger(CommandDecoder.class);
    private static final int LENGTH_FILED_LENGTH = Integer.BYTES;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (!byteBuf.isReadable(LENGTH_FILED_LENGTH)) {
            return;
        }
        byteBuf.markReaderIndex();
        int length = byteBuf.readInt() - LENGTH_FILED_LENGTH;

        if (byteBuf.readableBytes() < length) {
            byteBuf.resetReaderIndex();
            return;
        }

        Header header = null;
        try {
            header = decodeHeader(ctx, byteBuf);
        } catch (Exception e) {
            logger.error("decodeHeader error", e);
        }
        // 编码解码出现异常应该捕获、并且完成打印。
        int payloadLength = length - header.length();
        byte[] bytes = new byte[payloadLength];
        byteBuf.readBytes(bytes);
        list.add(new Command(header, bytes));
    }

    protected abstract Header decodeHeader(ChannelHandlerContext ctx, ByteBuf byteBuf);


}
