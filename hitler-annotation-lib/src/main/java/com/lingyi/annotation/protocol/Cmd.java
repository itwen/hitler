package com.lingyi.annotation.protocol;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author sunchuanwen
 * @time 2019/5/7.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Cmd {
    String targetClass() default "";
    String originClass() default "";
    String targetMethod () default "";
    CmdType funType();
    VariantsType buildType() default VariantsType.TYPE_DEFAULT;
}
