package org.event.controller;

import org.apache.ibatis.annotations.Delete;
import org.event.anno.Menu;
import org.event.pojo.Article;
import org.event.pojo.PageBean;
import org.event.pojo.Result;
import org.event.pojo.UserRole;
import org.event.pojo.dao.UserRoleDao;
import org.event.service.UserRoleService;
import org.event.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/userRole")
//@Menu(menuName = "权限管理")
public class UserRoleController {
    @Autowired
    private UserRoleService userRoleService;
    @GetMapping
    @Menu(menuName = "用户角色管理")
    public Result<PageBean<UserRoleDao>> list(Integer pageNum,
                                          Integer pageSize,
                                          @RequestParam(required = false) Integer roleId,
                                          @RequestParam(required = false) String nickname) {
        PageBean<UserRoleDao> pageBean = userRoleService.list(pageNum,pageSize,roleId,nickname);
        return Result.success(pageBean);
    }
    @PutMapping
    @Menu(menuName = "用户角色管理")
    public Result update(@RequestBody UserRole userRole) {
        userRoleService.update(userRole);
        return Result.success();
    }
    @DeleteMapping
    @Menu(menuName = "用户角色管理")
    public Result delete(@RequestParam(required = true)  Integer roleId,@RequestParam(required = true) Integer userId) {
        userRoleService.delete(userId,roleId);
        return Result.success();
    }
    @PostMapping
    @Menu(menuName = "用户角色管理")
    public Result add(@RequestBody UserRole userRole) {
        userRoleService.add(userRole);
        return Result.success();
    }
}
