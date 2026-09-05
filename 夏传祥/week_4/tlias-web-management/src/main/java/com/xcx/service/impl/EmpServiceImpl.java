package com.xcx.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xcx.mapper.EmpMapper;
import com.xcx.pojo.Emp;
import com.xcx.pojo.PageBean;
import com.xcx.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmpServiceImpl implements EmpService {
    @Autowired
    private EmpMapper empMapper;
    /*@Override
    public PageBean page(Integer page, Integer pageSize) {
        PageBean pageBean = new PageBean();
        Long total = empMapper.count();
        List rows = empMapper.list((page-1)*pageSize, pageSize);
        pageBean.setTotal(total);
        pageBean.setRows(rows);
        return pageBean;
    }*/

    //分页查询
    @Override
    public PageBean page(Integer page, Integer pageSize, String name, Short gender,
                         LocalDate start, LocalDate end) {
        //设置分页参数
        PageHelper.startPage(page, pageSize);

        //执行查询
        List<Emp> rows = empMapper.list(name, gender, start, end);
        Page<Emp> p = (Page<Emp>) rows;

        PageBean pageBean = new PageBean(p.getTotal(), p.getResult());
        return pageBean;
    }

    //批量删除
    @Override
    public void delete(Integer[] ids) {
        empMapper.delete(ids);
    }

    @Override
    public void save(Emp emp) {
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.insert(emp);
    }

    @Override
    public Emp get(Integer id) {
        Emp emp = empMapper.get(id);
        return emp;
    }

    @Override
    public void update(Emp emp) {
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.update(emp);
    }

    @Override
    public Emp login(Emp emp) {
        return empMapper.getByUsernameAndPassword(emp);
    }
}
