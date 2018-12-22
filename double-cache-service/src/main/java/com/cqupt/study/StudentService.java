package com.cqupt.study;

import com.cqupt.study.pojo.Student;

/**
 * @Description: StudentService
 * @Author: hetiantian
 * @Date:2018/12/22 20:50 
 * @Version: 1.0
 */
public interface StudentService {
    /**
     * 更新Student信息
     *
     * @param student
     * @return
     * */
    int saveStudent(Student student);

    /**
     * 根据id删除某个Student信息
     *
     * @param id
     * @return
     * */
    int removeStudent(int id);

    /**
     * 根据id更新某个Student信息
     *
     * @param student
     * @return
     * */
    int updateStudent(Student student);

    /**
     * 根据id查找某一个Student信息
     *
     * @param id
     * @return
     * */
    Student findStudent(int id);
}
