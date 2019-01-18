# double-cache
> 查询mysql数据库时，同样的输入需要不止一次获取值或者一个查询需要做大量运算时，很容易会想到使用redis缓存。但是如果查询并发量特别大的话，请求redis服务也会特别耗时，这种场景下，将redis迁移到本地减少查询耗时是一种常见的解决方法

### 项目结构说明
- double-cache-web 应用模块，请求入口
- double-cache-service 业务层模块
- double-cache-dao 持久层模块
- double-cache-common 公共模块，redis工具类，guava chche抽象类在该模块中

### 分支说明
- master  
基础版本
- cache_annotation_20190114  
在基础版本上添加基于注解的方法使用多级缓存

### 数据库准备
- 数据库的创建
```
create database double_cache
```

- student表的创建
```
CREATE TABLE `student` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `gmt_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8
```

### 多级缓存基本架构
![基本架构.png](https://user-gold-cdn.xitu.io/2018/12/25/167e5fbb01171093?w=916&h=718&f=png&s=18833)
说明：存储选择了`mysql`、`redis`和`guava cache`。
`mysql`作为持久化，`redis`作为分布式缓存， `guava cache`作为本地缓存。二级缓存其实就是在`redis`上面再架了一层`guava cahe`
<br>
![二级缓存.png](https://user-gold-cdn.xitu.io/2018/12/25/167e5fbb02ebbc3d?w=754&h=587&f=png&s=17812)


### guava cache简单介绍
`guava cache`和`concurrent hashmap`类似，都是k-v型存储，但是`concurrent hashmap`只能显示的移除元素，而`guava cache`当内存不够用时或者存储超时时会自动移除，具有缓存的基本功能

### 封装guava cache 
- 抽象类：SuperBaseGuavaCache.java
```
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
    protected Long duration = 10L;

    /**
     * 缓存失效单位，默认为5s
     */
    protected TimeUnit timeUnit = TimeUnit.SECONDS;

    /**
     * 返回Loading cache(单例模式的)
     *
     * @return LoadingCache<K, V>
     * */
    private LoadingCache<K, V> getCache() {
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
            getCache().invalidateAll(keys);
            log.info("批量清除缓存, keys为：{}", keys);
        } else {
            getCache().invalidateAll();
            log.info("清除了所有缓存");
        }
    }

    /**
     * 清除某个key的缓存
     * */
    public void invalidateOne(K key) {
        getCache().invalidate(key);
        log.info("清除了guava cache中的缓存, key为：{}", key);
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
        } catch (ExecutionException e) {
            log.error("获取guava cache中的缓存值出错, {}");
        }

        return cacheValue;
    }
}
```
抽象类说明：
- 1.双重锁检查并发安全的获取`LoadingCache`的单例对象
- `expireAfterWrite()`方法指定`guava cache`中键值对的过期时间，默认缓存时长为10s
- `maximumSize()`方法指定内存中最多可以存储的键值对数量，超过这个数量，`guava cache`将采用LRU算法淘汰键值对
- 这里采用CacheLoader的方式加载缓存值，需要实现`load()`方法。当调用`guava cache`的`get()`方法时，如果`guava cache`中存在将会直接返回值，否则调用`load()`方法将值加载到`guava cache`中。在该类中，`load`方法中是两个抽象方法，需要子类去实现，一个是`getLoadData()` 方法，这个方法一般是从数据库中查找数据，另外一个是`getLoadDataIfNull()`方法，当`getLoadData()`方法返回null值时调用，`guava cache`通过返回值是否为null判断是否需要进行加载，`load()`方法中返回null值将会抛出`InvalidCacheLoadException`异常：
- `invalidateOne()`方法主动失效某个key的缓存
- `batchInvalidate()`方法批量清除缓存或清空所有缓存，由传入的参数决定
-  `putIntoCache()`方法显示的将键值对存入缓存
-  `getCacheValue()`方法返回缓存中的值

- 抽象类的实现类：StudentGuavaCache.java
```
@Component
@Slf4j
public class StudentGuavaCache extends SuperBaseGuavaCache<Long, Student> {
    @Resource
    private StudentDAO studentDao;

    @Resource
    private RedisService<Long, Student> redisService;

    /**
     * 返回加载到内存中的数据，从redis中查找
     *
     * @param key key值
     * @return V
     * */
    @Override
    Student getLoadData(Long key) {
        Student student = redisService.get(key);
        if (student != null) {
            log.info("根据key：{} 从redis加载数据到guava cache", key);
        }
        return student;
    }

    /**
     * 调用getLoadData返回null值时自定义加载到内存的值
     *
     * @param key
     * @return
     * */
    @Override
    Student getLoadDataIfNull(Long key) {
        Student student = null;
        if (key != null) {
            Student studentTemp = studentDao.findStudent(key);
            student = studentTemp != null ? studentTemp : new Student();
        }

        log.info("从mysql中加载数据到guava cache中, key:{}", key);

        //此时在缓存一份到redis中
        redisService.set(key, student);
        return student;
    }
}
```
实现父类的`getLoadData()`和`getLoadDataIfNull()`方法
- `getLoadData()`方法返回redis中的值
- `getLoadDataIfNull()`方法如果redis缓存中不存在，则从mysql查找，如果在mysql中也查找不到，则返回一个空对象


### 查询
- 流程图：
![查询.png](https://user-gold-cdn.xitu.io/2018/12/25/167e5fbb03872681?w=964&h=664&f=png&s=20075)
    - 1.查询本地缓存是否命中
    - 2.本地缓存不命中查询redis缓存
    - 3.redis缓存不命中查询mysql
    - 4.查询到的结果都会被load到本地缓存中在返回
- 代码实现：
```
public Student findStudent(Long id) {
        if (id == null) {
            throw new ErrorException("传参为null");
        }

        return studentGuavaCache.getCacheValue(id);
    }
```


### 删除
- 流程图：
![删除.png](https://user-gold-cdn.xitu.io/2018/12/25/167e5fbb039ec33c?w=1240&h=193&f=png&s=15503)

- 代码实现：
```
@Transactional(rollbackFor = Exception.class)
    public int removeStudent(Long id) {
        //1.清除guava cache缓存
        studentGuavaCache.invalidateOne(id);
        //2.清除redis缓存
        redisService.delete(id);
        //3.删除mysql中的数据
        return studentDao.removeStudent(id);
    }
```

### 更新
- 流程图：
![更新.png](https://user-gold-cdn.xitu.io/2018/12/25/167e5fbb03fa2be7?w=1240&h=182&f=png&s=14881)

- 代码实现：
```
 @Transactional(rollbackFor = Exception.class)
    public int updateStudent(Student student) {
        //1.清除guava cache缓存
        studentGuavaCache.invalidateOne(student.getId());
        //2.清除redis缓存
        redisService.delete(student.getId());
        //3.更新mysql中的数据
        return studentDao.updateStudent(student);
    }
```
更新和删除就最后一步对mysql的操作不一样，两层缓存都是删除的
<br/>
<br/>
<br/>

附：[原文地址](https://juejin.im/post/5c224cd3f265da610e801e8a)
