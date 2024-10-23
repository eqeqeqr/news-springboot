package org.event.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.event.mapper.PermissionMapper;
import org.event.mapper.RoleMapper;
import org.event.mapper.RolePermissionMapper;
import org.event.mapper.UserRoleMapper;
import org.event.pojo.*;
import org.event.pojo.dao.PermissionDao;
import org.event.pojo.dao.PermissionShowDao;
import org.event.service.PermissionService;
import org.event.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public List<PermissionDao> list() {
        return permissionMapper.list();
    }

    @Override
    public PageBean<PermissionDao> pageList(Integer pageNum, Integer pageSize, String name) {
        PageBean<PermissionDao> pageBean=new PageBean<>();
        PageHelper.startPage(pageNum,pageSize);
        Page<PermissionDao> page = permissionMapper.pageList(name);
        pageBean.setItems(page.getResult());
        pageBean.setTotal(page.getTotal());

        return pageBean;
    }

    @Override
    public void add(Permission permission) {

        Permission permission1 =permissionMapper.getByName();
        if (permission1!=null){
            throw new RuntimeException("菜单已存在");
        }
        permission.setCreateTime(LocalDateTime.now());
        permission.setCreateUser(ThreadLocalUtil.getUserId());
        permissionMapper.add(permission);
    }

    @Override
    public void delete(Integer id) {
        List<RolePermission> list=rolePermissionMapper.getByPermissionId(id);
        if (!list.isEmpty()){
            throw new RuntimeException("该权限下存在角色，无法删除");
        }
        permissionMapper.deleteById(id);
    }

    @Override
    public List<PermissionDao> showList() {
        Integer userIdd=ThreadLocalUtil.getUserId();
        UserRole userRole = userRoleMapper.getByUserId(userIdd);
        if (userRole==null){
            return null;
        }
        List<RolePermission> list=rolePermissionMapper.getByRoleId(userRole.getRoleId());
        if (list.isEmpty()){
           return null;
        }
        List<PermissionDao> permissionDaoList=new ArrayList<>();
        for (RolePermission rolePermission:list){
            PermissionDao permissionDao=permissionMapper.getByRoleId(rolePermission.getPermissionId());
            if (permissionDao.getParentId()!=0){
                for (PermissionDao parentPermissionDao:permissionDaoList){
                    if (parentPermissionDao.getId().equals(permissionDao.getParentId())){
                        parentPermissionDao.getChildrenList().add( permissionDao);
                    }
                }
            }
            permissionDaoList.add(permissionDao);
        }
        return permissionDaoList;
    }

    @Override
    public List<PermissionDao>  parentList() {
        List<PermissionDao> list=permissionMapper.list();
        List<PermissionDao> parentList = list.stream().filter(permissionDao -> permissionDao.getParentId() == 0).collect(Collectors.toList());
        return parentList;
    }

    @Override
    public Permission findByName(String menuName) {
        return permissionMapper.getByName();
    }

    @Override
    public Permission getById(Integer parentId) {
        return permissionMapper.getById(parentId);
    }
}
