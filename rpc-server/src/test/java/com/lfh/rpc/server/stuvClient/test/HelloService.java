package com.lfh.rpc.server.stuvClient.test;

import com.lfh.rpc.server.nameservice.MetaData;
import java.net.URI;
import java.util.List;
import org.assertj.core.util.Lists;

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

    @Override
    public MetaData getMetaData(String msg) {
        List<URI> localhost = Lists.newArrayList(URI.create("localhost"));
        MetaData metaData = new MetaData();
        metaData.put("hello", localhost);
        return metaData;
    }

    @Override
    public String sayHello(String msg, String echo) {
        return msg + echo;
    }

    @Override
    public String sayHello(MetaData msg, String echo) {
        return null;
    }
}
