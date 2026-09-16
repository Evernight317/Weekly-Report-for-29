package org.example.tlias.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.tlias.pojo.Dept;
import org.example.tlias.pojo.Result;
import org.example.tlias.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("depts")
public class DeptController {
    @Autowired
    private DeptService service;
    @GetMapping
    public Result list(){
        List<Dept> deptList=service.list();
        log.info("查询全部部门数据");
        return Result.success(deptList);
    }

    @GetMapping("/{id}")
    public Result getbyid(@PathVariable Integer id){
        Dept dept=service.getById(id);
        log.info("根据id查询");
        return Result.success(dept);
    }
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        log.info("根据id删除部门数据");
        service.delete(id);
        return Result.success();
    }

    @PostMapping
    public Result add(@RequestBody Dept dept){
        log.info("添加部门数据");
        service.add(dept);
        return Result.success();
    }

    @PutMapping()
    public Result update(@RequestBody Dept dept){
        log.info("修改部门信息");
        service.update(dept);
        return Result.success();
    }
}
