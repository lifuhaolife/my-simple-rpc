package com.lfh.rpc.api.common;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lfh
 * @version 1.0
 * @date 2023/12/2 21:01
 */
@Target({ElementType.TYPE_USE, ElementType.ANNOTATION_TYPE})
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Singleton {
}
