package com.cqupt.study.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 查找双重缓存注解
 *
 * @Author: hetiantian
 * @Date:2019/1/14 20:49 
 * @Version: 1.0
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DoubleCacheSelect {
    /**
     * 缓存的key值
     * */
    String key();
}
