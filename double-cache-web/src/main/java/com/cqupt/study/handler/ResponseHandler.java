package com.cqupt.study.handler;

import com.cqupt.study.exception.ErrorException;
import com.cqupt.study.response.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一返回结果异常处理类
 *
 * @author hetiantian
 * @version 1.0
 * @Date 2019/08/02 10:16
 */
@ControllerAdvice
@ResponseBody
public class ResponseHandler {
    private static Logger logger = LoggerFactory.getLogger(com.cqupt.study.handler.ResponseHandler.class);

    @ExceptionHandler({com.cqupt.study.exception.ErrorException.class})
    public BaseResponse customException(ErrorException e, HttpServletRequest request) {
        logger.error(request.getRequestURI() + ":服务运行异常",e);
        return BaseResponse.fail("10001", e.getMsg());
    }

    @ExceptionHandler({Exception.class})
    public BaseResponse otherResponse(Exception e, HttpServletRequest request) {
        logger.error(request.getRequestURI() + ":自定义异常",e);
        return BaseResponse.fail(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e.getMessage());
    }
}
