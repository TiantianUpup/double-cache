package com.cqupt.study.dao;

import com.cqupt.study.pojo.Student;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @Description: student实体类
 * @Author: hetiantian
 * @Date:2018/12/22 20:36 
 * @Version: 1.0
 */
@Repository
public interface StudentDAO {
    /**
     * 更新Student信息
     *
     * @param student
     * @return
     * */
    @Insert("insert into student(NAME) VALUES(#{name})")
    int saveStudent(Student student);

    /**
     * 根据id删除某个Student信息
     *
     * @param id
     * @return
     * */
    @Delete("delete from student where id = #{id}")
    int removeStudent(Long id);

    /**
     * 根据id更新某个Student信息
     *
     * @param student
     * @return
     * */
    @Update("update student set name = #{name} where id = #{id}")
    int updateStudent(Student student);

    /**
     * 根据id查找某一个Student信息
     *
     * @param id
     * @return
     * */
    @Select("select * from student where id = #{id}")
    Student findStudent(Long id);
}
