package com.lfh.rpc.server.test.data;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/13 23:21
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String say(String msg) {
        return "echo" + msg;
    }
}
