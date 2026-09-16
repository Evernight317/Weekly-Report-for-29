package org.example.tlias.service;

import org.example.tlias.pojo.Dept;

import java.util.List;

public interface DeptService {
    List<Dept> list();
    void delete(Integer id);
    void add(Dept dept);
    void update(Dept dept);

    Dept getById(Integer id);
}
