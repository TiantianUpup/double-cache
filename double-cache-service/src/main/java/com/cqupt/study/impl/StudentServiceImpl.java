package com.cqupt.study.impl;

import com.cqupt.study.pojo.Student;
import com.cqupt.study.dao.StudentDao;
import com.cqupt.study.StudentGuavaCache;
import com.cqupt.study.StudentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description: StudentService实现类
 *
 * @Author: hetiantian
 * @Date:2018/12/22 20:51 
 * @Version: 1.0
 */
@Service
public class StudentServiceImpl implements StudentService {
    @Resource
    private StudentGuavaCache studentGuavaCache;

    @Resource
    private StudentDao studentDao;
    /**
     * 更新Student信息
     *
     * @param student
     * @return
     * */
    @Override
    public int saveStudent(Student student) {
        return 0;
    }

    /**
     * 根据id删除某个Student信息
     *
     * @param id
     * @return
     * */
    @Override
    public int removeStudent(int id) {
        return 0;
    }

    /**
     * 根据id更新某个Student信息
     *
     * @param student
     * @return
     * */
    @Override
    public int updateStudent(Student student) {
        return 0;
    }

    /**
     * 根据id查找某一个Student信息
     *
     * @param id
     * @return
     * */
    @Override
    public Student findStudent(int id) {
        return null;
    }
}
