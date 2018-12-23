package com.cqupt.study.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @Description: Student实体类
 *
 * @Author: hetiantian
 * @Date:2018/12/22 20:35 
 * @Version: 1.0
 */
@Data
public class Student {
    /**
     * 主键唯一标示符
     * */
    private Long id;

    /**
     * 姓名
     * */
    private String name;

    /**
     * 数据生成时间
     * */
    private Date gmtCreate;

    /**
     * 数据修改时间
     * */
    private Date gmtModified;
}
