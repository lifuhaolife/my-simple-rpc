package com.lfh.rpc.server.seriaize;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/3 20:44
 */
public class SerializeException extends RuntimeException {


    public SerializeException(String msg) {
        super(msg);
    }

    public SerializeException(Throwable throwable) {
        super(throwable);
    }
}
