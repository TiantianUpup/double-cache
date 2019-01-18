package com.cqupt.study.aspect;

import com.cqupt.study.annotation.DoubleCacheDelete;
import com.cqupt.study.exception.ErrorException;
import com.cqupt.study.guava.cache.StudentGuavaCache;
import com.cqupt.study.pojo.Student;
import com.cqupt.study.redis.RedisService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    /**
     * 表达式解析器
     * */
    ExpressionParser parser = new SpelExpressionParser();

    /**
     * 获取方法参数
     * */
    LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

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
        Method method = ((MethodSignature) pjd.getSignature()).getMethod();

        //获得参数名
        String[] params = discoverer.getParameterNames(method);

        //获得参数值
        Object[] object = pjd.getArgs();

        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < params.length; i++) {
            context.setVariable(params[i], object[i]);
        }


        //解析SpEL表达式
        if (doubleCacheDelete.key() == null) {
            throw new ErrorException("@DoubleCacheDelete注解中key值定义不为null");
        }

        String key = parser.parseExpression(doubleCacheDelete.key()).getValue(context, String.class);
        System.out.println("key: " + key);
        if (key != null) {
            //1.清除guava cache缓存
            studentGuavaCache.invalidateOne(Long.valueOf(key));
            //2.清除redis缓存
            redisService.delete(Long.valueOf(key));
        } else {
            throw new ErrorException("@DoubleCacheDelete注解中key值定义不存在，请检查是否和方法参数相同");
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
