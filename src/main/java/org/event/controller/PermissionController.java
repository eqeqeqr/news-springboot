package org.event.controller;

import org.apache.ibatis.annotations.Delete;
import org.event.anno.Menu;
import org.event.pojo.PageBean;
import org.event.pojo.Permission;
import org.event.pojo.Result;
import org.event.pojo.Role;
import org.event.pojo.dao.PermissionDao;
import org.event.pojo.dao.PermissionShowDao;
import org.event.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
//@Menu(menuName = "权限管理")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;
    @GetMapping
    public Result<List<PermissionDao>> list(){
        return Result.success(permissionService.list());
    }
    @GetMapping("/showList")
    public Result<List<PermissionDao>> showList(){
        List<PermissionDao> list= permissionService.showList();
        return Result.success(list);
    }
    @GetMapping("/pageList")
    @Menu(menuName = "权限设置")
    public Result<PageBean<PermissionDao>> pageList(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) String name
    ){
        PageBean<PermissionDao> pageBean = permissionService.pageList(pageNum,pageSize,name);
        return Result.success(pageBean);
    }
    @PostMapping
    @Menu(menuName = "权限设置")
    public Result add(@RequestBody Permission permission){
        permissionService.add(permission);
        return Result.success();
    }
    @DeleteMapping
    @Menu(menuName = "权限设置")
    public Result delete(Integer id){
        permissionService.delete(id);
        return Result.success();
    }
    @GetMapping("/parentList")
    public Result<List<PermissionDao>> parentList(){
        List<PermissionDao> parentList= permissionService.parentList();
        return Result.success(parentList);
    }
}
