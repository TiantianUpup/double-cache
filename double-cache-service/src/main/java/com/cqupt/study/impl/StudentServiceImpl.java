package com.cqupt.study.impl;

import com.cqupt.study.redis.RedisService;
import com.cqupt.study.exception.ErrorException;
import com.cqupt.study.pojo.Student;
import com.cqupt.study.dao.StudentDAO;
import com.cqupt.study.guava.cache.StudentGuavaCache;
import com.cqupt.study.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private StudentDAO studentDao;

    @Resource
    private RedisService<Long, Student> redisService;

    /**
     * 更新Student信息
     *
     * @param student
     * @return
     * */
    @Override
    public int saveStudent(Student student) {
        return studentDao.saveStudent(student);
    }

    /**
     * 根据id删除某个Student信息
     *
     * @param id
     * @return
     * */
    @Override
    public int removeStudent(Long id) {
        return studentDao.removeStudent(id);
    }

    /**
     * 根据id更新某个Student信息
     *
     * @param student
     * @return
     * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateStudent(Student student) {
        //1.清除guava cache缓存
        studentGuavaCache.invalidateOne(student.getId());
        //2.清除redis缓存
        redisService.delete(student.getId());
        //3.更新mysql中的数据
        int result = studentDao.updateStudent(student);
        //4.缓存双删
        redisService.delete(student.getId());
        studentGuavaCache.invalidateOne(student.getId());
        return result;
    }

    /**
     * 根据id查找某一个Student信息
     *
     * @param id
     * @return
     * */
    @Override
    public Student findStudent(Long id) {
        if (id == null) {
            throw new ErrorException("传参为null");
        }

        Student student = studentGuavaCache.getCacheValue(id);

        if (student.getId() == null) {
            throw new ErrorException("未查找到对应的学生信息");
        }

        return student;
    }
}
