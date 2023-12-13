package com.lfh.rpc.server.transport;

import com.lfh.rpc.server.service.RequestHandlerDispatch;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/7 23:29
 */
public interface TransportServer {

    void start(RequestHandlerDispatch requestHandlerDispatch, int port) throws Exception;

    void stop();
}
