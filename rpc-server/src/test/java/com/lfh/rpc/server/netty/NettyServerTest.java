package com.lfh.rpc.server.netty;

import com.lfh.rpc.server.transport.netty.NettyServer;
import java.io.IOException;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/1 21:36
 */
class NettyServerTest {


    public static void main(String[] args) {
        try (NettyServer nettyServer = new NettyServer(8099)){
            nettyServer.startServer();

            System.in.read();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}