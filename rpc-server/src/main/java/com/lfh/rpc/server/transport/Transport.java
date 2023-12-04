package com.lfh.rpc.server.transport;

import com.lfh.rpc.server.transport.protocol.Command;
import java.util.concurrent.CompletableFuture;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/4 0:33
 */
public interface Transport {


    /**
     * 发送请求命令
     * @param command
     * @return 服务端返回结果
     */
    CompletableFuture<Command> send(Command command);
}
