package com.lfh.rpc.server.service;

import com.lfh.rpc.server.transport.protocol.Command;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/4 23:05
 */
public interface RequestHandler {

    /**
     * 处理请求
     * @param requestCommand
     * @return
     */
    Command handler(Command requestCommand);


    /**
     * 支持的数据类型
     * @return
     */
    int type();
}
