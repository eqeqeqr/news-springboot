package org.event.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.event.anno.Menu;
import org.event.mapper.UserMapper;
import org.event.mapper.UserRoleMapper;
import org.event.pojo.Permission;
import org.event.pojo.dao.PermissionShowDao;
import org.event.service.PermissionService;
import org.event.service.RolePermissionService;
import org.event.service.UserRoleService;
import org.event.service.UserService;
import org.event.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class MenuAccessAspect {
    @Autowired
    RolePermissionService rolePermissionService;
    @Autowired
    PermissionService permissionService;

    @Pointcut("@annotation(org.event.anno.Menu)")
    public void menuAccess() {}
    @Before("menuAccess()")
    public void checkMenuAccess(JoinPoint joinPoint) {
        //获取当前用户
        //获取当前用户角色
        //获取当前菜单
        //判断当前用户是否拥有当前菜单的访问权限
        PermissionShowDao list = rolePermissionService.list();
        //获取方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Menu menu = method.getAnnotation(Menu.class);
        boolean flag = false;
        if (menu != null){
            String name = menu.menuName();
            flag=checkMenuAccess(name, list);
        }
        //如果当前用户没有权限，抛出异常
        if (!flag){
            throw new RuntimeException("当前用户无权限");
        }
    }
    public boolean checkMenuAccess(String name, PermissionShowDao list) {


        for (Permission permission : list.getPermissions()){
            if (permission.getName().equals(name)){
                if (permission.getParentId()==0){
                    return true;
                }else {
                    if (checkMenuParentAccess(permission.getParentId())){
                    System.out.println("父类菜单存在"+permission.getParentId()+permissionService.getById(permission.getParentId()).getName());
                       return checkMenuAccess(permissionService.getById(permission.getParentId()).getName(), list);
                    }
                }
               // break;
            }
        }
        return false;
    }
    public boolean checkMenuParentAccess(Integer parentId) {
        Permission permission=permissionService.getById(parentId);
        if (permission!=null){
            return true;//代表父类菜单存在
        }
        return false;
    }
}
