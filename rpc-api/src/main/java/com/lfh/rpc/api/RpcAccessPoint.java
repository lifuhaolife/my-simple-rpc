package com.lfh.rpc.api;

import java.net.URI;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/11/30 21:14
 */
public interface RpcAccessPoint {


    /**
     * 获取远程服务的引用
     * @param uri
     * @param serviceClass
     * @return
     * @param <T>
     */
    <T> T getRemoteService(URI uri, Class<T> serviceClass);


    /**
     * 注册服务实现
     * @param service
     * @param serviceClass
     * @return
     * @param <T>
     */
    <T> URI addServiceProvider(T service, Class<T> serviceClass);

}
