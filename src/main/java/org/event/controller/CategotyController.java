package org.event.controller;

import org.event.anno.Menu;
import org.event.pojo.Category;
import org.event.pojo.PageBean;
import org.event.pojo.Result;
import org.event.pojo.dao.PermissionDao;
import org.event.service.CategotyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
//@Menu(menuName = "文章分类")
public class CategotyController {
    @Autowired
    private CategotyService categotyService;
    @Menu(menuName = "分类展示")
    @PostMapping
    public Result add(@RequestBody @Validated(Category.Add.class) Category category) {
        categotyService.add(category);
        return Result.success();
    }

    @GetMapping
    public Result<List<Category>> list() {
        List<Category>list=categotyService.list();
        return Result.success(list);
    }
    @Menu(menuName = "分类展示")
    @GetMapping("/pageList")
    public Result<PageBean<Category>> pageList(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) String name
    ){
        PageBean<Category> pageBean =categotyService.pageList(pageNum,pageSize,name);
        return Result.success(pageBean);
    }
    @Menu(menuName = "分类展示")
    @GetMapping("detail")
    public Result<Category> detail(Integer id) {
        System.out.println("id:"+id);
        Category category=categotyService.findById(id);
        return Result.success(category);
    }
    @Menu(menuName = "分类展示")
    @PutMapping
    public Result update(@RequestBody @Validated(Category.Update.class) Category category) {
        categotyService.update(category);
        return Result.success();
    }
    @Menu(menuName = "分类展示")
    @DeleteMapping
    public Result delete(Integer id) {
        categotyService.deleteById(id);
        return Result.success();
    }
}
