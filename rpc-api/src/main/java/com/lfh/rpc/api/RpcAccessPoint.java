package com.lfh.rpc.api;

import com.lfh.rpc.api.common.ServiceLoaderSupport;
import java.io.Closeable;
import java.net.URI;
import java.util.Collection;
import java.util.ServiceLoader;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/11/30 21:14
 */
public interface RpcAccessPoint extends Closeable {


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


    /**
     * 获取注册中心引用
     * @param nameServiceUri
     * @return
     */
    default NameService getNameService(URI nameServiceUri)  {
        Collection<NameService> nameServices = ServiceLoaderSupport.serviceLoadAll(NameService.class);
        for (NameService nameService : nameServices) {
            if (nameService.supportSchemes().contains(nameServiceUri.getScheme())) {
                nameService.connect(nameServiceUri);
                return nameService;
            }
        }
        return null;
    }

    Closeable startServer() throws Exception;


}
