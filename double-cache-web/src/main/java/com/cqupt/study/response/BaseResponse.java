package com.cqupt.study.response;

/**
 * @Description: 前端返回展示信息
 *
 * @Author: hetiantian
 * @Date:2018/12/23 16:01 
 * @Version: 1.0
 */
public class BaseResponse<T> {
    private boolean isSuccess;

    /**
     * 返回的数据
     * */
    private T data;
}
