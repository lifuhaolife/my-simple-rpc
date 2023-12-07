package com.lfh.rpc.server.service;

import com.lfh.rpc.api.common.Constants;
import com.lfh.rpc.api.common.ServiceLoaderSupport;
import com.lfh.rpc.api.common.Singleton;
import com.lfh.rpc.server.seriaize.SerializeSupport;
import com.lfh.rpc.server.transport.protocol.Code;
import com.lfh.rpc.server.transport.protocol.Command;
import com.lfh.rpc.server.transport.protocol.Header;
import com.lfh.rpc.server.transport.protocol.ResponseHeader;
import com.lfh.rpc.server.transport.protocol.request.RpcRequest;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/4 23:10
 */
@Singleton
public class DefaultRequestHandler implements RequestHandler {

    /**
     * 后续考虑直接使用元数据、类信息、method、保存下来。
     */
    private static final Logger logger = LoggerFactory.getLogger(ServiceProviderRegistry.class);

    private ServiceProviderRegistry serviceProviderRegistry = ServiceProviderRegistry.getInstance();

    @Override
    public Command handler(Command requestCommand) {
        Header header = requestCommand.getHeader();

        RpcRequest request = SerializeSupport.parse(requestCommand.getPayload());

        try {
            Object serviceProvider = serviceProviderRegistry.getServiceProvider(request.getInterfaceName());

            if (null != serviceProvider) {
                Object[] args = SerializeSupport.dynamicParamParse(request.getSerializedArguments());
                Class[] paramTypeClass = new Class[args.length];
                for (int i = 0; i < args.length; i++) {
                    paramTypeClass[i] = args[i].getClass();
                }

                Method method = serviceProvider.getClass().getMethod(request.getMethodName(), paramTypeClass);
                Object object = method.invoke(serviceProvider, args);
                Class<?> returnType = method.getReturnType();
                return new Command(new ResponseHeader(header.getVersion(),
                        header.getRequestId(), type(), Code.SUCCESS.getCode(), null), SerializeSupport.serialize(object, returnType));
            }
            logger.warn("No service Provider of {}#{}(String)!", request.getInterfaceName(), request.getMethodName());
            // 如果没找到，返回NO_PROVIDER错误响应。
            return new Command(new ResponseHeader(header.getVersion(),
                    header.getRequestId(), type(), Code.NO_PROVIDER.getCode(), "No provider!"), new byte[0]);
        } catch (Throwable e) {
            // 发生异常，返回UNKNOWN_ERROR错误响应。
            logger.warn("Exception: ", e);
            return new Command(new ResponseHeader(header.getVersion(),
                    header.getRequestId(), type(), Code.UNKNOWN_ERROR.getCode(), "unknown error !"), new byte[0]);
        }
    }

    @Override
    public int type() {
        return Constants.ServiceType.SERVICE_RPC_REQUEST;
    }

}
