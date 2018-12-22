package com.cqupt.study;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 封装的Guava Cache缓存
 *
 * @Author: hetiantian
 * @Date:2018/12/20 16:11
 * @Version: 1.0
 */
public abstract class SuperBaseGuavaCache<K, V> {
    /**
     * 缓存对象
     * */
    private LoadingCache<K, V> cache;

    /**
     * 缓存最大容量，默认为10
     * */
    private Integer maximumSize = 10;

    /**
     * 返回Loading cache(单例模式的)
     *
     * @param duration 失效时长
     * @param unit 失效时长单位
     * @return LoadingCache<K, V>
     * */
    public LoadingCache<K, V> getCache(long duration, TimeUnit unit) {
        if (cache == null) {
            synchronized (SuperBaseGuavaCache.class) {
                if (cache == null) {
                    CacheBuilder<Object, Object> tempCache = CacheBuilder.newBuilder()
                        .expireAfterWrite(duration, unit);

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
                            V target = getLoadData(key) != null ? getLoadData(key) : getLoadDataIfNull();
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
     * @return V
     * */
    abstract V getLoadDataIfNull();

    /**
     * 清除缓存(可以批量清除，也可以清除全部)
     *
     * @param keys 需要清除缓存的key值
     * */
    public void batchInvalidate(List<K> keys) {
        if (keys != null ) {
            cache.invalidateAll(keys);
        }

        cache.invalidateAll();
    }

    /**
     * 清除某个key的缓存
     * */
    public void invalidateOne(K key) {
        cache.invalidate(key);
    }

    /**
     * 写入缓存
     *
     * @param key 键
     * @param value 键对应的值
     * */
    public void putIntoCache(K key, V value) {
        cache.put(key, value);
    }
}
