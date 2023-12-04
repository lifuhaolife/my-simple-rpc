package com.lfh.rpc.server.stuvClient.test;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/4 23:58
 */
public class HelloService implements Hello {
    @Override
    public String sayHello(String msg) {
        return "say" + msg;
    }
}
