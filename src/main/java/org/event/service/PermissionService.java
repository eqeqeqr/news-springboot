package org.event.service;

import org.event.pojo.PageBean;
import org.event.pojo.Permission;
import org.event.pojo.Role;
import org.event.pojo.dao.PermissionDao;
import org.event.pojo.dao.PermissionShowDao;

import java.util.List;

public interface PermissionService {
    List<PermissionDao> list();

    PageBean<PermissionDao> pageList(Integer pageNum, Integer pageSize, String name);

    void add(Permission permission);

    void delete(Integer id);


    List<PermissionDao> showList();

    List<PermissionDao> parentList();

    Permission findByName(String menuName);

    Permission getById(Integer parentId);
}
