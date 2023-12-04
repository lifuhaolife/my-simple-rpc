package com.lfh.rpc.server.client;

import com.lfh.rpc.server.transport.Transport;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/4 21:31
 */
public interface ServiceStub {

    void setTransport(Transport transport);
}
