package org.event.service;

import org.event.pojo.Category;
import org.event.pojo.PageBean;
import org.event.pojo.dao.PermissionDao;

import java.util.List;

public interface CategotyService {
    void add(Category category);

    List<Category> list();

    Category findById(Integer id);

    void update(Category category);



    void deleteById(Integer id);

    PageBean<Category> pageList(Integer pageNum, Integer pageSize, String name);
}
