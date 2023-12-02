package com.lfh.rpc.server.nameservice.test.data;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/2 21:06
 */
public class HelloServiceImpl implements Hello {
    @Override
    public void sayHello(String message) {
        System.out.println("say hello world");
    }
}
