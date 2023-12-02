package com.lfh.rpc.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/11/30 20:17
 */
public interface NameService {

    /**
     * 提供默认注册发现地址
     * @return
     */
    URI getNameServiceUrl();

    /**
     * 当前注册发现支持的类型
     * @return
     */
    Collection<String> supportSchemes();

    /**
     * 连接注册中心（保持连接）
     * @param nameServiceUri
     */
    void connect(URI nameServiceUri);;

    /**
     * 服务注册
     * @param serviceName 服务名称
     * @param serviceAddress 服务ip地址
     */
    void registerService(String serviceName, URI serviceAddress) throws IOException;


    /**
     * 查询服务地址
     * @param serviceName 查询服务信息
     * @return
     */
    URI lookupService(String serviceName) throws IOException;

}
