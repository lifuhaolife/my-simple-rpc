package com.lfh.rpc.server.stuvClient.test;

import com.lfh.rpc.server.nameservice.MetaData;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/4 23:57
 */
public interface Hello {

    String sayHello(String msg);

    MetaData getMetaData(String msg);

    String sayHello(String msg, String echo);

    String sayHello(MetaData msg, String echo);


}
