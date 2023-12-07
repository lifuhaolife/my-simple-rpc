package com.lfh.rpc.server.client;

import com.lfh.rpc.api.common.Constants;
import com.lfh.rpc.api.common.RequestIdSupport;
import com.lfh.rpc.server.seriaize.SerializeSupport;
import com.lfh.rpc.server.transport.Transport;
import com.lfh.rpc.server.transport.protocol.Code;
import com.lfh.rpc.server.transport.protocol.Command;
import com.lfh.rpc.server.transport.protocol.Header;
import com.lfh.rpc.server.transport.protocol.ResponseHeader;
import com.lfh.rpc.server.transport.protocol.request.RpcRequest;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/4 21:32
 */
public abstract class AbstractStub implements ServiceStub {

    private Logger logger = LoggerFactory.getLogger(AbstractStub.class);
    private Transport transport;

    @Override
    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    protected byte[] invokeRemote(RpcRequest request) throws Exception {

        Header header = new Header(Constants.Version.VERSION_1_0_0,
                RequestIdSupport.next(),
                Constants.ServiceType.SERVICE_RPC_REQUEST);
        Command command = new Command(header, SerializeSupport.dynamicParamSerialize(request));

        try {

            Command responseCommand = transport.send(command).get();
            ResponseHeader responseHeader = (ResponseHeader) responseCommand.getHeader();
            if (responseHeader.getCode() == Code.SUCCESS.getCode()) {
                return responseCommand.getPayload();
            } else {
                throw new Exception(responseHeader.getErrMsg());
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.info("error", e);
            throw new Exception();
        }
    }
}
