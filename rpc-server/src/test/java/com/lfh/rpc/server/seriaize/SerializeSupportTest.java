package com.lfh.rpc.server.seriaize;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

import static com.lfh.rpc.server.seriaize.SerializeSupport.dynamicParamParse;
import static com.lfh.rpc.server.seriaize.SerializeSupport.dynamicParamSerialize;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/7 0:19
 */
class SerializeSupportTest {

    @Test
    void dynamicParamSerializeTest() {

        byte[] bytes = dynamicParamSerialize("hello", "world");
        Stream.of(dynamicParamParse(bytes)).forEach(System.out::println);
    }
}