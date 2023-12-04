package com.lfh.rpc.server.client;

import com.lfh.rpc.server.transport.Transport;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/4 21:51
 */
public interface StubFactory {


    /**
     * 工厂创建桩方法
     * @param tClass
     * @return
     * @param <T>
     */
    <T> T create(Transport transport, Class<T> tClass);
}
