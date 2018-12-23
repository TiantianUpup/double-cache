package com.cqupt.study;

import com.cqupt.study.dao.StudentDAO;
import com.cqupt.study.pojo.Student;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description: guava cache实现类
 *
 * @Author: hetiantian
 * @Date:2018/12/22 20:56 
 * @Version: 1.0
 */
@Component
public class StudentGuavaCache extends SuperBaseGuavaCache<Long, Student> {
    @Resource
    private StudentDAO studentDao;

    /**
     * 返回加载到内存中的数据，一般从数据库中加载
     *
     * @param key key值
     * @return V
     * */
    @Override
    Student getLoadData(Long key) {
        return studentDao.findStudent(key);
    }

    /**
     * 调用getLoadData返回null值时自定义加载到内存的值
     *
     * @return 空的一个Student对象
     * */
    @Override
    Student getLoadDataIfNull() {
        return new Student();
    }
}
