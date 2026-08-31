package com.xcx.mapper;

import com.xcx.pojo.Emp;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface EmpMapper {
    /*//查询员工数量
    @Select("select count(*) from emp")
    public Long count();

    //查询员工
    @Select("select * from emp limit #{start},#{pageSize}")
    public List<Emp> list(Integer start, Integer pageSize);*/

    //@Select("select * from emp")
    List<Emp> list(String name, Short gender,
                          LocalDate start, LocalDate end);


    void delete(Integer[] ids);

    //新增员工
    @Insert("insert into emp (username, name, gender, image, job, entrydate, dept_id, create_time, update_time)" +
            "VALUES (#{username},#{name},#{gender},#{image},#{job},#{entrydate},#{deptId},#{createTime},#{updateTime})")
    void insert(Emp emp);

    //根据id查询员工
    @Select("select * from emp where id=#{id}")
    Emp get(Integer id);


    //修改员工
    void update(Emp emp);

    //根据用户名和密码查询员工
    @Select("select * from emp where username=#{username} and password=#{password}")
    Emp getByUsernameAndPassword(Emp emp);
}
