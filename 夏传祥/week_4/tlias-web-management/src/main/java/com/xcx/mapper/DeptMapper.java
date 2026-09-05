package com.xcx.mapper;

import com.xcx.pojo.Dept;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DeptMapper {

    // 查询所有部门
    @Select("select * from dept")
    List<Dept> list();
    // 删除部门
    @Delete("delete from dept where id=#{id}")
    void delete(Integer id);
    // 添加部门
    @Insert("insert into dept (name, create_time, update_time) values (#{name}, #{createTime}, #{updateTime})")
    void add(Dept dept);
    //根据id查询部门
    @Select("select * from dept where id=#{id}")
    Dept get(Integer id);
    //修改部门
    @Update("update dept set name=#{name}, update_time=#{updateTime} where id=#{id}")
    void update(Dept dept);
}
