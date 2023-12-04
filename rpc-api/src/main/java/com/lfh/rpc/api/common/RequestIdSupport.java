package com.lfh.rpc.api.common;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/4 21:35
 */
public class RequestIdSupport {

    private static final Snowflake snowflake = IdUtil.getSnowflake();

    public static String next() {
        return String.valueOf(snowflake.nextId());
    }

}
