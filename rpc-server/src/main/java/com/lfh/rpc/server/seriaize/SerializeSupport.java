package com.lfh.rpc.server.seriaize;

import com.lfh.rpc.api.common.ServiceLoaderSupport;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/3 21:15
 */
public class SerializeSupport {

    private static final Logger logger = LoggerFactory.getLogger(SerializeSupport.class);

    private static final Map<Class<?>/*序列化对象类型*/, Serializer<?>/*序列化实现*/> serializerMap = new HashMap<>();

    private static final Map<Byte/*序列化类型*/, Class<?>/*序列化对象类型*/> typeMap = new HashMap<>();


    static {
        for (Serializer serializer : ServiceLoaderSupport.serviceLoadAll(Serializer.class)) {
            registerType(serializer.type(), serializer.getSerializeClass(), serializer);
            logger.info("Found serializer , class :{} , type : {}",
                    serializer.getSerializeClass().getCanonicalName(),
                    serializer.type());
        }
    }

    private static <E> void registerType(byte type, Class<E> serializeClass, Serializer<E> serializer) {
        serializerMap.put(serializeClass, serializer);
        typeMap.put(type, serializeClass);
    }

    private static byte parseEntryType(byte[] buffer) {
        return buffer[0];
    }


    public static <E> E parse(byte[] buffer) {
        return parse(buffer, 0, buffer.length);
    }

    private static <E> E parse(byte[] buffer, int offset, int length) {
        byte type = parseEntryType(buffer);
        Class<E> aClass = (Class<E>) typeMap.get(type);
        if (aClass == null) {
            throw new SerializeException(String.format("Unknown entry type: %d!", type));
        }
        return parse(buffer, offset + 1, length - 1, aClass);
    }

    private static <E> E parse(byte[] buffer, int offset, int length, Class<E> aClass) {
        Object entry = serializerMap.get(aClass).parse(buffer, offset, length);
        if (aClass.isAssignableFrom(entry.getClass())) {
            return (E) entry;
        }
        throw new SerializeException("Type mismatch!");
    }

    public static <E> byte[] serialize(E entry) {
        Serializer<E> serializer = (Serializer<E>) serializerMap.get(entry.getClass());
        if (serializer == null) {
            throw new SerializeException(String.format("Unknown entry class type: %s", entry.getClass().getCanonicalName()));
        }
        byte[] bytes = new byte[serializer.size(entry) + 1];
        bytes[0] = serializer.type();
        serializer.serialize(entry, bytes, 1, bytes.length - 1);
        return bytes;
    }

    public static <E> byte[] serialize(E entry, Class aClass) {
        Serializer<E> serializer = (Serializer<E>) serializerMap.get(aClass);
        if (serializer == null) {
            throw new SerializeException(String.format("Unknown entry class type: %s", aClass.getCanonicalName()));
        }
        byte[] bytes = new byte[serializer.size(entry) + 1];
        bytes[0] = serializer.type();
        serializer.serialize(entry, bytes, 1, bytes.length - 1);
        return bytes;
    }
    
        

    /**
     * 请求参数的序列化与反序列化
     * 序列化协议的标准：
     * // 在保持所有的序列化不变的情况下、以及所有的对象都实现了序列化的情况下
     * 1. 首先第一个字节判断有多少个参数
     * 2. 第三个字节代表长度序列化的长度
     * 3. 接下来的字节代表参数类型
     *
     * @param entry
     * @param <E>
     * @return
     */
    public static <E> byte[] dynamicParamSerialize(E... entry) {
        if (entry.length == 1) {
            return serialize(entry[0]);
        }
        int argsNum = entry.length;
        List<byte[]> serializeList = new ArrayList<>();
        for (E e : entry) {
            byte[] bytes = SerializeSupport.serialize(e);
            serializeList.add(bytes);
        }
        int byteLength = serializeList.stream().mapToInt(bytes -> bytes.length).sum();
        byte[] resultBytes = new byte[(int) ( 2 + (argsNum) * Integer.BYTES + byteLength)];
        resultBytes[0] = -2;
        resultBytes[1] = (byte) argsNum;
        ByteBuffer buffer = ByteBuffer.wrap(resultBytes, 2, resultBytes.length - 2);
        for (byte[] bytes : serializeList) {
            buffer.putInt(bytes.length);
            buffer.put(bytes);
        }
        return resultBytes;
    }

    public static Object[] dynamicParamParse(byte[] bytes) {
        if (bytes[0] != -2) {
            return new Object[]{parse(bytes)};
        }

        int argsNum = bytes[1];
        ByteBuffer buffer = ByteBuffer.wrap(bytes, 2, bytes.length - 2);
        Object[] objects = new Object[argsNum];
        for (int i = 0; i < argsNum; i++) {
            int length = buffer.getInt();
            byte[] tempBytes = new byte[length];
            buffer.get(tempBytes, 0, length);
            Object parsedObject = SerializeSupport.parse(tempBytes);
            objects[i] = parsedObject;
        }
        return objects;
    }


}
