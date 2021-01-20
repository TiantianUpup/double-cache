package com.cqupt.study.guava.cache;

import com.cqupt.study.pojo.Student;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 封装的Guava Cache缓存
 *
 * @Author: hetiantian
 * @Date:2018/12/20 16:11
 * @Version: 1.0
 */
@Slf4j
public abstract class SuperBaseGuavaCache<K, V> {

    /**
     * 缓存对象
     * */
    private volatile LoadingCache<K, V> cache;

    /**
     * 缓存最大容量，默认为10
     * */
    protected Integer maximumSize = 10;

    /**
     * 缓存失效时长
     * */
    protected Long duration = 5L;

    /**
     * 缓存失效单位，默认为5s
     */
    protected TimeUnit timeUnit = TimeUnit.SECONDS;

    /**
     * 返回Loading cache(单例模式的)
     *
     * @return LoadingCache<K, V>
     * */
    public LoadingCache<K, V> getCache() {
        if (cache == null) {
            synchronized (SuperBaseGuavaCache.class) {
                if (cache == null) {
                    CacheBuilder<Object, Object> tempCache = null;

                    if (duration > 0 && timeUnit != null) {
                        tempCache = CacheBuilder.newBuilder()
                            .expireAfterWrite(duration, timeUnit);
                    }

                    //设置最大缓存大小
                    if (maximumSize > 0) {
                        tempCache.maximumSize(maximumSize);
                    }

                    //加载缓存
                    cache = tempCache.build( new CacheLoader<K, V>() {
                        //缓存不存在或过期时调用
                        @Override
                        public V load(K key) throws Exception {
                            //不允许返回null值
                            V target = getLoadData(key) != null ? getLoadData(key) : getLoadDataIfNull(key);
                            return target;
                        }
                    });
                }


            }
        }

        return cache;
    }

    /**
     * 返回加载到内存中的数据，一般从数据库中加载
     *
     * @param key key值
     * @return V
     * */
    abstract V getLoadData(K key);

    /**
     * 调用getLoadData返回null值时自定义加载到内存的值
     *
     * @param key
     * @return V
     * */
    abstract V getLoadDataIfNull(K key);

    /**
     * 清除缓存(可以批量清除，也可以清除全部)
     *
     * @param keys 需要清除缓存的key值
     * */
    public void batchInvalidate(List<K> keys) {
        if (keys != null ) {
            List<K> targetKeys = new ArrayList<>();
            for (K key : keys) {
                if (getCacheValue(key) != null) {
                    targetKeys.add(key);
                }
            }
            getCache().invalidateAll(targetKeys);
            log.info("批量清除缓存, keys为：{}", targetKeys);
        } else {
            getCache().invalidateAll();
            log.info("清除了所有缓存");
        }
    }

    /**
     * 清除某个key的缓存
     * */
    public void invalidateOne(K key) {
        if (getCacheValue(key) != null) {
            getCache().invalidate(key);
            log.info("清除guava cache中的缓存, key为：{}", key);
        }
    }

    /**
     * 写入缓存
     *
     * @param key 键
     * @param value 键对应的值
     * */
    public void putIntoCache(K key, V value) {
        getCache().put(key, value);
    }

    /**
     * 获取某个key对应的缓存
     *
     * @param key
     * @return V
     * */
    public V getCacheValue(K key) {
        V cacheValue = null;
        try {
            cacheValue = getCache().get(key);
            System.out.println("cacheValue" +cacheValue);
        } catch (ExecutionException e) {
            log.error("获取guava cache中的缓存值出错, {}");
        }

        return cacheValue;
    }
}
