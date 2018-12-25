package com.cqupt.study.controller;

import com.cqupt.study.StudentService;
import com.cqupt.study.pojo.Student;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Description: Student Web Controller
 *
 * @Author: hetiantian
 * @Date:2018/12/22 21:05 
 * @Version: 1.0
 */
@RestController
@RequestMapping("/api/student")
public class StudentController {
    @Resource
    private StudentService studentService;

    @PostMapping
    public Object saveStudent(@RequestBody Student student) {
        return studentService.saveStudent(student);
    }

    @DeleteMapping("/{id}")
    public Object removeStudent(@PathVariable("id") Long id) {
        return studentService.removeStudent(id);
    }

    @GetMapping("/{id}")
    public Object findStudent(@PathVariable("id") Long id) {
        return studentService.findStudent(id);
    }

    @PutMapping
    public Object updateStudent(@RequestBody Student student) {
        return studentService.updateStudent(student);
    }
}
