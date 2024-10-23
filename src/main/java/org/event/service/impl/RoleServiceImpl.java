package org.event.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.event.mapper.RoleMapper;
import org.event.mapper.RolePermissionMapper;
import org.event.mapper.UserRoleMapper;
import org.event.pojo.PageBean;
import org.event.pojo.Role;
import org.event.pojo.UserRole;
import org.event.pojo.dao.RoleDao;
import org.event.service.RoleService;
import org.event.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Override
    public List<Role> list() {
        return roleMapper.list();
    }

    @Override
    public PageBean<RoleDao> pageList(Integer pageNum, Integer pageSize, String roleName, Integer roleWeight) {
        PageBean pageBean=new PageBean<>();
        PageHelper.startPage(pageNum,pageSize);
        Page<RoleDao> page = roleMapper.pageList(roleName,roleWeight);
        List<RoleDao> roleList = page.getResult();
        pageBean.setItems(roleList);
        pageBean.setTotal(page.getTotal());
        return pageBean;
    }

    @Override
    public void add(Role role) {
        if (role.getWeight()==null){
            role.setWeight(0);
        }
        if (roleMapper.getByName(role.getName())!=null){
            throw new RuntimeException("该角色已存在");
        }
        role.setCreateTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());
        role.setUpdateUser(ThreadLocalUtil.getUserId());
        role.setCreateUser(ThreadLocalUtil.getUserId());
        roleMapper.add(role);
    }

    @Override
    public void delete(Integer id) {
        if (!userRoleMapper.getByRoleId(id).isEmpty()||!rolePermissionMapper.getByRoleId(id).isEmpty()){

            throw new RuntimeException("该角色下存在用户或权限，无法删除");
        }
        roleMapper.deleteById(id);
    }

    @Override
    public void update(Role role) {
        if (!roleMapper.getById(role.getId()).getName().equals(role.getName())){
            if (roleMapper.getByName(role.getName())!=null){
                throw new RuntimeException("该角色已存在");
            }
        }
        roleMapper.update(role);
    }


}
