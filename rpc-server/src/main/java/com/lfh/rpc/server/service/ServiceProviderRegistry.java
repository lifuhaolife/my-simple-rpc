package com.lfh.rpc.server.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/11/30 22:56
 */
public class ServiceProviderRegistry {

    /**
     * 后续考虑直接使用元数据、类信息、method、保存下来。
     */
    private static final Logger logger = LoggerFactory.getLogger(ServiceProviderRegistry.class);
    private static Map<String /*service name*/, Object /* service provider*/> serviceProviders = new ConcurrentHashMap<>();


    public static synchronized <T> void addServiceProviders(Class<? extends T> serviceClass, T serviceProvider) {

        serviceProviders.put(serviceClass.getCanonicalName(), serviceProvider);
        logger.info("Add service : {}, provider : {}",
                serviceClass.getCanonicalName(),
                serviceProvider.getClass().getCanonicalName());
    }
}
