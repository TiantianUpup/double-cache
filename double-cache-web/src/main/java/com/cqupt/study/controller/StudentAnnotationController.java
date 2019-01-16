package com.cqupt.study.controller;

import com.cqupt.study.StudentService;
import com.cqupt.study.annotation.DoubleCacheDelete;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description: 基于注解的方式使用缓存
 *
 * @Author: hetiantian
 * @Date:2019/1/15 15:24 
 * @Version: 1.0
 */
@RestController
@RequestMapping("/api/annotation/student")
public class StudentAnnotationController {
    @Resource
    private StudentService studentService;

    @DeleteMapping("/{id}")
    @DoubleCacheDelete(key = "#{id}")
    public Object removeStudent(@PathVariable("id") Long id) {
        return studentService.removeStudent(id);
    }
}
