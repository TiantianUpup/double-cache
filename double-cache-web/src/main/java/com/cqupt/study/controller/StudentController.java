package com.cqupt.study.controller;

import com.cqupt.study.StudentService;
import com.cqupt.study.pojo.Student;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description: Student Web Controller
 *
 * @Author: hetiantian
 * @Date:2018/12/22 21:05 
 * @Version: 1.0
 */
@RestController("/api/student")
public class StudentController {
    @Resource
    private StudentService studentService;

    @PostMapping
    public Object saveStudent(@RequestBody Student student) {
        return studentService.saveStudent(student);
    }
}
