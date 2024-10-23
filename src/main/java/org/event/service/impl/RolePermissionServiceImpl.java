package org.event.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.event.mapper.PermissionMapper;
import org.event.mapper.RolePermissionMapper;
import org.event.mapper.UserMapper;
import org.event.mapper.UserRoleMapper;
import org.event.pojo.*;
import org.event.pojo.dao.PermissionShowDao;
import org.event.pojo.dao.RolePermissionDao;
import org.event.service.RolePermissionService;
import org.event.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    public PermissionShowDao list() {
        //获取用户信息
        UserRole userRole=userRoleMapper.getByUserId(ThreadLocalUtil.getUserId());
        //获取角色关联的权限信息
        List<RolePermission> list = rolePermissionMapper.getByRoleId(userRole.getRoleId());
        //装 权限的集合
        List<Permission> showDaos= new ArrayList<>();
        for (RolePermission rolePermission:list){
            //根据用户的角色对应的权限id获取详细的权限信息
            showDaos.add(permissionMapper.getByRoleId(rolePermission.getPermissionId()));
        }
        PermissionShowDao permissionShowDao =new PermissionShowDao();
        permissionShowDao.setId(ThreadLocalUtil.getUserId());
        permissionShowDao.setPermissions(showDaos);

        return permissionShowDao;
    }

    @Override
    public PageBean<RolePermissionDao> pageList(Integer pageNum, Integer pageSize, Integer roleId, Integer permissionId) {
        PageBean<RolePermissionDao> pageBean=new PageBean<>();
        PageHelper.startPage(pageNum,pageSize);
        Page<RolePermissionDao> page = rolePermissionMapper.list(roleId,permissionId);
        List<RolePermissionDao> items = page.getResult();
        for(RolePermissionDao rolePermissionDao:items){
            User user =userMapper.findByUserId(rolePermissionDao.getUpdateUser());
            if (user==null)continue;
            rolePermissionDao.setUpdateUserName(user.getNickname());
        }
        pageBean.setTotal(page.getTotal());
        pageBean.setItems(page.getResult());
        return pageBean;
    }

    @Override
    public void add(RolePermission rolePermission) {
        RolePermission rolePermission1=rolePermissionMapper.getByRoleIdAndPermissionId(rolePermission.getRoleId(),rolePermission.getPermissionId());
        if (rolePermission1!=null){
            throw new RuntimeException("该权限已存在");
        }
        rolePermission.setCreateTime(LocalDateTime.now());
        rolePermission.setUpdateTime(LocalDateTime.now());
        rolePermission.setUpdateUser(ThreadLocalUtil.getUserId());
        rolePermissionMapper.add(rolePermission);
    }

    @Override
    public void delete(Integer roleId, Integer permissionId) {
        rolePermissionMapper.deleteByRoleIdAndPermissionId(roleId,permissionId);
    }

    @Override
    public List<RolePermission> rpList(Integer roleId) {

        return rolePermissionMapper.getByRoleId(roleId);
    }



}
