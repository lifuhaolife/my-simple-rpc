package com.lfh.rpc.api.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/2 20:49
 */
public class ServiceLoaderSupport {

    private static final Map<String, Object> singletonService = new HashMap<>();

    public static <T> T serviceLoader(Class<T> tClass) {
        return StreamSupport.stream(ServiceLoader.load(tClass).spliterator(), false)
                .map(ServiceLoaderSupport::singletonFilter)
                .findFirst().orElseThrow(ServiceLoadException::new);
    }

    public static <T> Collection<T> serviceLoadAll(Class<T> serviceClass) {
        return StreamSupport.stream(ServiceLoader.load(serviceClass).spliterator(), false)
                .map(ServiceLoaderSupport::singletonFilter)
                .collect(Collectors.toList());
    }

    private static <T> T singletonFilter(T service) {
        if (service.getClass().isAnnotationPresent(Singleton.class)) {
            String canonicalName = service.getClass().getCanonicalName();
            Object singletonInstance = singletonService.putIfAbsent(canonicalName, service);
            return singletonInstance == null ? service : (T) singletonInstance;

        }
        return service;
    }
}
