package com.lfh.rpc.server.seriaize;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/3 16:42
 */
public interface Serializer<T> {


    /**
     * 序列化对象的byte长度计算；
     *
     * @param entry
     * @return
     */
    int size(T entry);

    /**
     * 完成对象序列化后他填充数组
     *
     * @param entry
     * @param bytes
     * @param offset
     * @param length
     */
    void serialize(T entry, byte[] bytes, int offset, int length);

    /**
     * 根据数据类型完成反序列化
     *
     * @param bytes
     * @param offset
     * @param length
     * @return
     */
    T parse(byte[] bytes, int offset, int length);

    /***
     * 标志数据类型
     * @return
     */
    byte type();

    /**
     * 返回序列化对象的Class对象
     *
     * @return
     */
    Class<T> getSerializeClass();


}
