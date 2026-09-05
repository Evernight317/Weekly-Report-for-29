package com.xcx.service;

import com.xcx.pojo.Dept;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DeptService {
    //查询所有部门
    List<Dept> list();
    //删除部门
    void delete(Integer id);
    //添加部门
    void add(Dept dept);
    //根据id查询部门
    Dept get(Integer id);
    //修改部门
    void update(Dept dept);
}
