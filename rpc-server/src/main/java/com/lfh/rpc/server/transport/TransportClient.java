package com.lfh.rpc.server.transport;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeoutException;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/7 23:54
 */
public interface TransportClient extends Closeable {


    Transport createTransport(InetSocketAddress address, long connectTimeout) throws InterruptedException, TimeoutException;

    void close() throws IOException;
}
