package com.cqupt.study.redis.impl;

import com.cqupt.study.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Random;

/**
 * @Description: redis service实现类
 *
 * @Author: hetiantian
 * @Date:2018/12/23 20:28 
 * @Version: 1.0
 */
@Slf4j
@Service
public class RedisServiceImpl<K, V> implements RedisService<K, V> {
    @Resource
    private RedisTemplate redisTemplate;

    private ValueOperations<K, V> valueOperations;

    /**
     * 初始化valueOperation
     * */
    @PostConstruct
    public void init() {
        valueOperations = redisTemplate.opsForValue();
    }

    /**
     * 往redis缓存中存值
     *
     * @param key
     * @param value
     */
    @Override
    public void set(K key, V value) {
        //redis缓存失效时间随机，避免缓存穿透
        Random random = new Random();
        valueOperations.set(key, value, duration + random.nextInt(5), timeUnit);
        log.info("key={}, value={} 存入redis缓存", key, value);
    }

    /**
     * 根据key从redis中获取缓存值
     *
     * @param key
     */
    @Override
    public V get(K key) {
        V redisValue = valueOperations.get(key);
        log.info("从redis缓存中读取值, value为：{}", redisValue);
        return redisValue;
    }

    /**
     * 删除key的缓存
     *
     * @param key
     */
    @Override
    public void delete(K key) {
        if (get(key) != null) {
            redisTemplate.delete(key);
            log.info("删除redis中的缓存,key为：{}", key);
        }
    }
}
