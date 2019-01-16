package com.cqupt.study.aspect;

import com.cqupt.study.annotation.DoubleCacheDelete;
import com.cqupt.study.guava.cache.StudentGuavaCache;
import com.cqupt.study.pojo.Student;
import com.cqupt.study.redis.RedisService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Description:  查找双重缓存注解的切面
 *
 * @Author: hetiantian
 * @Date:2019/1/15 14:46 
 * @Version: 1.0
 */
@Aspect
@Component
public class DoubleCacheDeleteAspect {
    @Resource
    private StudentGuavaCache studentGuavaCache;

    @Resource
    private RedisService<Long, Student> redisService;

    /**
     * 在方法执行之前对注解进行处理
     *
     * @param pjd
     * @param doubleCacheDelete 注解
     * @return 返回中的值
     * */
    @Around("@annotation(com.cqupt.study.annotation.DoubleCacheDelete) && @annotation(doubleCacheDelete)")
    @Transactional(rollbackFor = Exception.class)
    public Object dealProcess(ProceedingJoinPoint pjd, DoubleCacheDelete doubleCacheDelete) {
        Object result = null;
        String key = doubleCacheDelete.key();
        if (key != null) {
            //1.清除guava cache缓存
            studentGuavaCache.invalidateOne(Long.valueOf(key));
            //2.清除redis缓存
            redisService.delete(Long.valueOf(key));
        }

        //执行目标方法
        try {
            result = pjd.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return result;
    }

}
