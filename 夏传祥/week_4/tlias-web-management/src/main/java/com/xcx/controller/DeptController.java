package com.xcx.controller;

import com.xcx.pojo.Dept;
import com.xcx.pojo.Result;
import com.xcx.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j//记录日志
@RestController//返回json数据
@RequestMapping("/depts")
public class DeptController {
    @Autowired
    private DeptService deptService;

    //查询所有部门
    //@RequestMapping(value = "/depts",method = RequestMethod.GET)
    @GetMapping
    public Result list() {
        log.info("查询所有部门");

        //调用service查询
        List<Dept> deptList=deptService.list();

        return Result.success(deptList);
    }

    //删除部门
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        log.info("删除部门，id为：{}",id);
        deptService.delete(id);
        return Result.success();
    }

    //添加部门
    @PostMapping
    public Result add(@RequestBody Dept dept){
        log.info("添加部门，名称为：{}",dept.getName());
        deptService.add(dept);
        return Result.success();
    }

    //根据id查询部门
    @GetMapping("/{id}")
    public Result get(@PathVariable Integer id){
        log.info("查询部门，id为：{}",id);
        Dept dept=deptService.get(id);
        return Result.success(dept);
    }

    //修改部门
    @PutMapping
    public Result update(@RequestBody Dept dept){
        log.info("修改部门，名称为：{}",dept.getName());
        deptService.update(dept);
        return Result.success();
    }
}
