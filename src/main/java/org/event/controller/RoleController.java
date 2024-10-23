package org.event.controller;

import org.event.anno.Menu;
import org.event.pojo.PageBean;
import org.event.pojo.Result;
import org.event.pojo.Role;
import org.event.pojo.dao.RoleDao;
import org.event.service.RoleService;
import org.event.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/role")
//@Menu(menuName = "权限管理")
public class RoleController {

    @Autowired
    private RoleService roleService;
    @GetMapping
    public Result<List<Role>> list(){
            return Result.success(roleService.list());
    }
    @GetMapping("/pageList")
    @Menu(menuName = "角色管理")
    public Result<PageBean<RoleDao>> pageList(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) String roleName,
            @RequestParam(required = false) Integer roleWeight
    ){
        if (roleWeight!=null&&(roleWeight<0||roleWeight>100)){
            throw new RuntimeException("权重范围0-100");
        }
        PageBean<RoleDao> pageBean = roleService.pageList(pageNum, pageSize, roleName, roleWeight);
        return Result.success(pageBean);
    }
    @PostMapping
    @Menu(menuName = "角色管理")
    public Result add(@RequestBody Role role){
        roleService.add(role);
        return Result.success();
    }
    @DeleteMapping
    @Menu(menuName = "角色管理")
    public Result delete(@RequestParam(required = true)  Integer id){
        roleService.delete(id);
        return Result.success();
    }
    @PutMapping
    @Menu(menuName = "角色管理")
    public Result update(@RequestBody Role role){
        role.setUpdateTime(LocalDateTime.now());
        role.setUpdateUser(ThreadLocalUtil.getUserId());
        roleService.update(role);
        return Result.success();
    }
}
