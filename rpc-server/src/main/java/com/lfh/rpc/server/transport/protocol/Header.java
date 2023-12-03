package com.lfh.rpc.server.transport.protocol;

import java.nio.charset.StandardCharsets;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/3 15:45
 */
public class Header {

    private String version;

    private String requestId;

    private int type;

    public Header(String version, String requestId, int type) {
        this.version = version;
        this.requestId = requestId;
        this.type = type;
    }

    public int length() {
        return version.getBytes(StandardCharsets.UTF_8).length +
                requestId.getBytes(StandardCharsets.UTF_8).length +
                Integer.BYTES;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "Header{" +
                "version='" + version + '\'' +
                ", requestId='" + requestId + '\'' +
                ", type=" + type +
                '}';
    }
}
