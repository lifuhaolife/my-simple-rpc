package com.lfh.rpc.server.seriaize;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/3 21:13
 */
public class StringSerializer implements Serializer<String> {
    @Override
    public int size(String entry) {
        return entry.getBytes(StandardCharsets.UTF_8).length;
    }

    @Override
    public void serialize(String entry, byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        buffer.put(entry.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String parse(byte[] bytes, int offset, int length) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    @Override
    public byte type() {
        return SerializeType.TYPE_STRING;
    }

    @Override
    public Class<String> getSerializeClass() {
        return String.class;
    }
}
