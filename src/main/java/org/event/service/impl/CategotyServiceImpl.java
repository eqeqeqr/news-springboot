package org.event.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.event.mapper.CategoryMapper;
import org.event.pojo.Article;
import org.event.pojo.Category;
import org.event.pojo.PageBean;
import org.event.service.CategotyService;
import org.event.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class CategotyServiceImpl implements CategotyService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public void add(Category category) {
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        Map<String, Object> map = ThreadLocalUtil.get();
        category.setCreateUser((Integer) map.get("id"));
        categoryMapper.add(category);
    }

    @Override
    public List<Category> list() {

        List<Category>categoryList= categoryMapper.list();
        return categoryList;
    }

    @Override
    public Category findById(Integer id) {

        Category category= categoryMapper.findById(id);
        return category;
    }

    @Override
    public void update(Category category) {
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.update(category);
    }

    @Override
    public void deleteById(Integer id) {
        categoryMapper.deleteById(id);
    }

    @Override
    public PageBean<Category> pageList(Integer pageNum, Integer pageSize, String name) {
        //1.创建pageBean
        PageBean<Category> pageBean = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        Page<Category> page = categoryMapper.pageList(name);
        pageBean.setTotal(page.getTotal());
        pageBean.setItems(page.getResult());
        return pageBean;
    }
}
