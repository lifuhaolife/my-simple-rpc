package com.lfh.rpc.server.transport;

import cn.hutool.core.thread.NamedThreadFactory;
import com.lfh.rpc.server.transport.protocol.response.ResponseFuture;
import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/5 21:23
 * <p>
 * desc:
 * 1. 用户缓存客户发送的请求信息、接受后完成释放填充消息、
 * 2. 完成完成背压机制、 客户端的最大请求数据限制
 */
public class InFlightRequests implements Closeable {

    private final static long TIMEOUT_SEC = 10L;
    private final Semaphore semaphore = new Semaphore(10);
    private final Map<String, ResponseFuture> futureMap = new ConcurrentHashMap<>();

    private final ScheduledExecutorService scheduledExecutorService =
            new ScheduledThreadPoolExecutor(2, new NamedThreadFactory("Rpc request thread pool", true), new ThreadPoolExecutor.AbortPolicy());

    private final ScheduledFuture scheduledFuture;

    public InFlightRequests() {
        scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(this::removeTimeoutFutures, TIMEOUT_SEC, TIMEOUT_SEC, TimeUnit.SECONDS);
    }

    public void put(ResponseFuture responseFuture) throws InterruptedException, TimeoutException {
        if (semaphore.tryAcquire(TIMEOUT_SEC, TimeUnit.SECONDS)) {
            futureMap.put(responseFuture.getRequestId(), responseFuture);
        } else {
            throw new TimeoutException();
        }
    }

    public ResponseFuture remove(String requestId) {
        ResponseFuture responseFuture = futureMap.get(requestId);
        if (null != responseFuture) {
            semaphore.release();
        }
        return responseFuture;
    }

    private void removeTimeoutFutures() {
        futureMap.entrySet().removeIf(entry -> {
            if (System.nanoTime() - entry.getValue().getTimestamp() > TIMEOUT_SEC * 1000000000L) {
                semaphore.release();
                return true;
            }
            return false;
        });
    }

    @Override
    public void close() throws IOException {
        scheduledFuture.cancel(true);
        scheduledExecutorService.shutdown();
    }
}
