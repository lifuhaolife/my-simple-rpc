package com.lfh.rpc.server.transport.protocol.response;

import com.lfh.rpc.server.transport.protocol.Command;
import java.util.concurrent.CompletableFuture;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/5 21:21
 */
public class ResponseFuture {


    private final String requestId;

    private final CompletableFuture<Command> future;

    private final long timestamp;


    public ResponseFuture(String requestId, CompletableFuture<Command> future) {
        this.requestId = requestId;
        this.future = future;
        this.timestamp = System.nanoTime();
    }

    public String getRequestId() {
        return requestId;
    }

    public CompletableFuture<Command> getFuture() {
        return future;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
