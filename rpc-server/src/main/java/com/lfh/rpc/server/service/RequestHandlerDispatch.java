package com.lfh.rpc.server.service;

import com.lfh.rpc.api.common.ServiceLoaderSupport;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/4 22:20
 */
public class RequestHandlerDispatch {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerDispatch.class);
    private Map<Integer, RequestHandler> handlerMap = new HashMap<>();
    private static RequestHandlerDispatch instance = null;

    public static RequestHandlerDispatch getInstance() {
        if (null == instance) {
            instance = new RequestHandlerDispatch();
        }
        return instance;
    }

    private RequestHandlerDispatch() {
        for (RequestHandler requestHandler : ServiceLoaderSupport.serviceLoadAll(RequestHandler.class)) {
            handlerMap.put(requestHandler.type(), requestHandler);
            logger.info("Load request handler, type: {}, class: {}.", requestHandler.type(), requestHandler.getClass().getCanonicalName());
        }
    }

    public RequestHandler dispatch(int type) {
        return handlerMap.get(type);
    }
}
