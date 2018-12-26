package com.cqupt.study.redis;

import java.util.concurrent.TimeUnit;

/**
 * @Description: redis service interface
 * @Author: hetiantian
 * @Date:2018/12/23 20:23 
 * @Version: 1.0
 */
public interface RedisService<K, V> {
    /**
     * 缓存失效时长
     * */
    long duration = 20L;

    /**
     * 缓存失效单位，默认为5s,和guava cache失效时长相同
     * */
    TimeUnit timeUnit = TimeUnit.SECONDS;

    /**
     * 往redis缓存中存值
     * */
    void set(K key, V value);

    /**
     * 根据key从redis中获取缓存值
     * */
    V get(K key);

    /**
     * 删除key的缓存
     * */
    void delete(K key);
}
