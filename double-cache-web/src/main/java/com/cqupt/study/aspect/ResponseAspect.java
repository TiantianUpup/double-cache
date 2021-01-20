package com.cqupt.study.aspect;

import com.cqupt.study.response.BaseResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @Description: web层统一返回格式切面
 *
 * @Author: hetiantian
 * @Date:2018/12/28 14:13 
 * @Version: 1.0
 */
@Aspect
@Component
public class ResponseAspect {
    @Around("execution(* com.cqupt.study.controller..*(..))")
    public Object controllerProcess(ProceedingJoinPoint pjd) throws Throwable {
        Object result = pjd.proceed();
        //如果controller不返回结果
        if (result == null) {
            return BaseResponse.success(null);
        }

        return BaseResponse.success(result);
    }
}
