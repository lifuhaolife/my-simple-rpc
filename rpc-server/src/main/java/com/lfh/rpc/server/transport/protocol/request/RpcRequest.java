package com.lfh.rpc.server.transport.protocol.request;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/4 0:36
 */
public class RpcRequest {

    private String interfaceName;

    private String methodName;

    private byte[] serializedArguments;


    public RpcRequest(String interfaceName, String methodName, byte[] serializedArguments) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.serializedArguments = serializedArguments;
    }
    public String getInterfaceName() {
        return interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public byte[] getSerializedArguments() {
        return serializedArguments;
    }
}
