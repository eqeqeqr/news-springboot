package org.event.controller;

import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.Delete;
import org.event.anno.Menu;
import org.event.pojo.PageBean;
import org.event.pojo.Result;
import org.event.pojo.RolePermission;
import org.event.pojo.dao.PermissionShowDao;
import org.event.pojo.dao.RolePermissionDao;
import org.event.pojo.dao.UserRoleDao;
import org.event.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rolePremission")
//@Menu(menuName = "权限管理")
public class RolePermissionController {
    @Autowired
    private RolePermissionService rolePermissionService;
    @GetMapping
    public Result<PermissionShowDao> list(){
        PermissionShowDao list= rolePermissionService.list();
        return Result.success(list);
    }
    @GetMapping("rpList")
    public Result<List<RolePermission>> rplist(Integer roleId){
        List<RolePermission> list= rolePermissionService.rpList(roleId);
        return Result.success(list);
    }
    @GetMapping("/pageList")
    @Menu(menuName = "角色权限管理")
    public Result<PageBean<RolePermissionDao>> pageList(Integer pageNum,
                                                    Integer pageSize,
                                                    @RequestParam(required = false) Integer roleId,
                                                    @RequestParam(required = false) Integer permissionId) {
        PageBean<RolePermissionDao> pageBean = rolePermissionService.pageList(pageNum,pageSize,roleId,permissionId);
        return Result.success(pageBean);
    }
    @PostMapping
    @Menu(menuName = "角色权限管理")
    public Result add(@RequestBody @Validated RolePermission rolePermission){
        rolePermissionService.add(rolePermission);
        return Result.success();
    }
    @DeleteMapping
    @Menu(menuName = "角色权限管理")
    public Result delete(@RequestParam(required = true) Integer roleId, @RequestParam(required = true) Integer permissionId){
        rolePermissionService.delete(roleId,permissionId);
        return Result.success();
    }

}

