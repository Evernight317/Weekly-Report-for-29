package com.xcx.service;

import com.xcx.pojo.Emp;
import com.xcx.pojo.PageBean;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface EmpService {
    //分页查询
    PageBean page(Integer page, Integer pageSize, String name, Short gender,
                  LocalDate start, LocalDate end);

    //批量删除
    void delete(Integer[] ids);

    //保存员工
    void save(Emp emp);

    //根据id查询员工
    Emp get(Integer id);


    //更新员工
    void update(Emp emp);

    //员工登录
    Emp login(Emp emp);
}
