package org.event.service;

import org.event.pojo.PageBean;
import org.event.pojo.RolePermission;
import org.event.pojo.dao.PermissionShowDao;
import org.event.pojo.dao.RolePermissionDao;

import java.util.List;

public interface RolePermissionService {
    PermissionShowDao list();

    PageBean<RolePermissionDao> pageList(Integer pageNum, Integer pageSize, Integer roleId, Integer permissionId);

    void add(RolePermission rolePermission);

    void delete(Integer roleId, Integer permissionId);

    List<RolePermission> rpList(Integer roleId);


}
