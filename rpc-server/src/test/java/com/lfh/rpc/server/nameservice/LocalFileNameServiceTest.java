package com.lfh.rpc.server.nameservice;

import com.lfh.rpc.api.NameService;
import com.lfh.rpc.api.common.ServiceLoaderSupport;
import com.lfh.rpc.server.nameservice.test.data.Hello;
import java.io.IOException;
import java.net.URI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/2 20:48
 */
class LocalFileNameServiceTest {

    @Test
    public void testNameService() throws IOException {

        NameService nameService = ServiceLoaderSupport.serviceLoader(NameService.class);
        URI nameServiceUrl = nameService.getNameServiceUrl();
        nameService.connect(nameServiceUrl);
        String canonicalName = Hello.class.getCanonicalName();
        nameService.registerService(canonicalName, nameServiceUrl);

        URI uri = nameService.lookupService(canonicalName);
        Assertions.assertEquals(nameServiceUrl.getPath(), uri.getPath());
    }
}