package com.xcx.controller;

import com.xcx.pojo.Emp;
import com.xcx.pojo.PageBean;
import com.xcx.pojo.Result;
import com.xcx.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpController {
    @Autowired
    private EmpService empService;

    //分页查询
    @GetMapping
    public Result page(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       String name, Short gender,
                       @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate begin,
                       @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate end){
        log.info("分页查询，参数：page={},pageSize={}",page,pageSize);

        PageBean pageBean = empService.page(page,pageSize,name,gender,begin,end);
        return Result.success(pageBean);
    }


    //批量删除
    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable Integer[] ids){
        log.info("批量删除，参数：{}",ids);
        empService.delete(ids);
        return Result.success();
    }


    //新增员工
    @PostMapping
    public Result save(@RequestBody Emp emp){
        log.info("新增员工，员工信息：{}",emp);
        empService.save(emp);
        return Result.success();
    }

    //根据id查询员工
    @GetMapping("/{id}")
    public Result get(@PathVariable Integer id){
        log.info("查询员工，id为：{}",id);
        Emp emp = empService.get(id);
        return Result.success(emp);
    }

    //更新员工信息
    @PutMapping
    public Result update(@RequestBody Emp emp){
        log.info("更新员工信息：{}",emp);
        empService.update(emp);
        return Result.success();
    }
}
