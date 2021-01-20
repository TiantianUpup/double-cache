package com.cqupt.study.exception;

import lombok.Data;

/**
 * @Description: 自定义异常
 *
 * @Author: hetiantian
 * @Date:2018/12/25 15:19 
 * @Version: 1.0
 */
@Data
public class ErrorException extends RuntimeException {
    private String msg;

    public ErrorException (String msg) {
        this.msg = msg;

    }


}
