package com.lfh.rpc.server;

import com.lfh.rpc.api.NameService;
import com.lfh.rpc.api.RpcAccessPoint;
import com.lfh.rpc.api.common.ServiceLoaderSupport;
import com.lfh.rpc.server.test.data.HelloService;
import com.lfh.rpc.server.test.data.HelloServiceImpl;
import java.io.Closeable;
import java.io.File;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/8 0:14
 */
public class NettyRpcAccessPointServerTest {


    private static final Logger logger = LoggerFactory.getLogger(NettyRpcAccessPointServerTest.class);

    public static void main(String[] args) {


        String canonicalName = HelloService.class.getCanonicalName();
        File tempFile = new File(System.getProperty("java.io.tmpdir"));
        File file = new File(tempFile, "my_rpc_name_service.data");
        logger.info("start Rpc Server");
        try (RpcAccessPoint rpcAccessPoint = ServiceLoaderSupport.serviceLoader(RpcAccessPoint.class);
             Closeable closeable = rpcAccessPoint.startServer();) {

            NameService nameService = rpcAccessPoint.getNameService(file.toURI());

            HelloService helloService = new HelloServiceImpl();
            URI uri = rpcAccessPoint.addServiceProvider(helloService, HelloService.class);
            nameService.registerService(canonicalName, uri);
            logger.info("开始提供服务,按任何建退出");
            System.in.read();
            logger.info("Bye !");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}