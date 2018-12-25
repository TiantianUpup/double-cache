package com.cqupt.study.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: Student实体类
 *
 * @Author: hetiantian
 * @Date:2018/12/22 20:35 
 * @Version: 1.0
 */
@Data
public class Student implements Serializable {
    /**
     * 序列号，自动生成
     * */
    private static final long serialVersionUID = -6339973031989737251L;

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
