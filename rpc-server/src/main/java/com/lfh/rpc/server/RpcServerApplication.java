package com.lfh.rpc.server;

import com.lfh.rpc.server.transport.netty.NettyServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RpcServerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(RpcServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        NettyServer nettyServer = new NettyServer(8099);
        nettyServer.startServer();
    }
}
