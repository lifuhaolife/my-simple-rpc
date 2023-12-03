package com.lfh.rpc.server.transport.protocol;

import java.nio.charset.StandardCharsets;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/3 16:28
 */
public class ResponseHeader extends Header {

    private int code;

    private String errMsg;

    public ResponseHeader(String version, String requestId, int type) {
        this(version, requestId, type, Code.SUCCESS.getCode(), null);

    }

    public ResponseHeader(String version, String requestId, int type, int code, String errMsg) {
        super(version, requestId, type);
        this.code = code;
        this.errMsg = errMsg;
    }

    @Override
    public int length() {
        return super.length() +
                Integer.BYTES + (errMsg == null ? 0 : errMsg.getBytes(StandardCharsets.UTF_8).length);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }


    @Override
    public String toString() {
        return "ResponseHeader{" +
                "version='" + getVersion() + '\'' +
                ", requestId='" + getRequestId() + '\'' +
                ", type=" + getType() +
                ", code=" + code +
                ", errMsg='" + errMsg + '\'' +
                '}';
    }
}
