package com.lfh.rpc.server.transport.protocol;

import java.nio.charset.StandardCharsets;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/3 15:45
 */
public class Command {

    protected Header header;

    private byte[] payload;

    public Command(Header header, byte[] payload) {
        this.header = header;
        this.payload = payload;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "Command{" +
                "header=" + header == null ? null : header.toString() +
                ", payload=" + new String(payload, StandardCharsets.UTF_8) +
                '}';
    }
}
