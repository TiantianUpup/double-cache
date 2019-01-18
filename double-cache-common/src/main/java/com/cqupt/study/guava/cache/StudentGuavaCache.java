package com.cqupt.study.guava.cache;

import com.cqupt.study.dao.StudentDAO;
import com.cqupt.study.pojo.Student;
import com.cqupt.study.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description: guava cache实现类
 *
 * @Author: hetiantian
 * @Date:2018/12/22 20:56 
 * @Version: 1.0
 */
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
            if (studentTemp != null) {
                log.info("从mysql中加载数据到guava cache中, key:{}", key);
            }
            student = studentTemp != null ? studentTemp : new Student();
        }

        //此时在缓存一份到redis中，空对象不会缓存到redis中
        if (student.getId() != null) {
            redisService.set(key, student);
        }
        return student;
    }

    /**
     * 清除某个key的缓存
     * */
    @Override
    public void invalidateOne(Long key) {
        if (getCacheValue(key).getId() != null) {
            getCache().invalidate(key);
            log.info("清除guava cache中的缓存, key为：{}", key);
        }
    }
}
