package org.example.tlias.mapper;

import org.apache.ibatis.annotations.*;
import org.example.tlias.pojo.Dept;
import java.util.List;

@Mapper
public interface DeptMapper {
    @Select("select * from dept")
    List<Dept> list();

    @Delete("delete from dept where id = #{id}")
    void delete(Integer id);

    @Insert("insert into dept(name,create_time,update_time) values(#{name},#{createTime},#{updateTime})")
    void insert(Dept dept);

    @Update("update dept set name=#{name},update_time=#{updateTime} where id=#{id}")
    void update(Dept dept);

    @Select("select * from dept where id = #{id}")
    Dept getById(Integer id);
}
