package org.event.service;

import org.event.pojo.PageBean;
import org.event.pojo.Role;
import org.event.pojo.dao.RoleDao;

import java.util.List;

public interface RoleService {
    List<Role> list();


    PageBean<RoleDao> pageList(Integer pageNum, Integer pageSize, String roleName, Integer roleWeight);

    void add(Role role);

    void delete(Integer id);

    void update(Role role);
}
