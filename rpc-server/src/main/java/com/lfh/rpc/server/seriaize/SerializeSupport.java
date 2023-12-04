package com.lfh.rpc.server.seriaize;

import com.lfh.rpc.api.common.ServiceLoaderSupport;
import java.util.HashMap;
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


}
