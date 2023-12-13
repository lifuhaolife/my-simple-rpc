package com.lfh.rpc.server;

import com.lfh.rpc.api.NameService;
import com.lfh.rpc.api.RpcAccessPoint;
import com.lfh.rpc.api.common.ServiceLoaderSupport;
import com.lfh.rpc.server.test.data.HelloService;
import java.io.File;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/13 23:23
 */
public class RpcClientTest {


    private static final Logger logger = LoggerFactory.getLogger(RpcClientTest.class);

    public static void main(String[] args) {

        File tempFile = new File(System.getProperty("java.io.tmpdir"));
        File file = new File(tempFile, "my_rpc_name_service.data");
        try (RpcAccessPoint rpcAccessPoint = ServiceLoaderSupport.serviceLoader(RpcAccessPoint.class)) {

            NameService nameService = rpcAccessPoint.getNameService(file.toURI());

            URI uri = nameService.lookupService(HelloService.class.getCanonicalName());

            HelloService remoteService = rpcAccessPoint.getRemoteService(uri, HelloService.class);

            String hello = remoteService.say("hello");

            logger.info("response : {}", hello);

        } catch (Throwable e) {
            logger.error("error", e);
        }
    }
}
