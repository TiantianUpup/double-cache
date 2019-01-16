package com.cqupt.study.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 删除双重缓存注解
 *
 * @Author: hetiantian
 * @Date:2019/1/14 20:47 
 * @Version: 1.0
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DoubleCacheDelete {
    /**
     * 缓存的key
     * */
    String key();
}
