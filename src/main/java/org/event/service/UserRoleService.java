package org.event.service;

import org.event.pojo.Article;
import org.event.pojo.PageBean;
import org.event.pojo.UserRole;
import org.event.pojo.dao.UserRoleDao;

public interface UserRoleService {
    public PageBean<UserRoleDao> list(Integer pageNum, Integer pageSize, Integer roleId, String nickname);

    void update(UserRole userRole);


    void delete(Integer userId, Integer roleId);

    void add(UserRole userRole);
}
