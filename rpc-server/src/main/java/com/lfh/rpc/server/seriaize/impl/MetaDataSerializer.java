package com.lfh.rpc.server.seriaize.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.lfh.rpc.server.nameservice.MetaData;
import com.lfh.rpc.server.seriaize.SerializeType;
import com.lfh.rpc.server.seriaize.Serializer;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/3 20:48
 */
public class MetaDataSerializer implements Serializer<MetaData> {
    @Override
    public int size(MetaData entry) {
        return JSON.toJSONString(entry).getBytes(StandardCharsets.UTF_8).length;
    }

    @Override
    public void serialize(MetaData entry, byte[] bytes, int offset, int length) {
        byte[] entryBytes = JSON.toJSONBytes(entry);
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        buffer.put(entryBytes);
    }

    @Override
    public MetaData parse(byte[] bytes, int offset, int length) {
        MetaData metaData = new MetaData();
        byte[] bufferBytes = new byte[length];
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        buffer.get(bufferBytes);
        Map<String, JSONArray> jsonArrayMap = JSON.parseObject(bufferBytes, Map.class);
        for (String key : jsonArrayMap.keySet()) {
            List<URI> uriList = new ArrayList<>();
            for (Object uri : jsonArrayMap.get(key)) {
                uriList.add(URI.create(uri.toString()));
            }
            metaData.put(key, uriList);
        }
        return metaData;
    }

    @Override
    public byte type() {
        return SerializeType.TYPE_METADATA;
    }

    @Override
    public Class<MetaData> getSerializeClass() {
        return MetaData.class;
    }
}
