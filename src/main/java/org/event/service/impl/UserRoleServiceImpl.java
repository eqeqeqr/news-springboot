package org.event.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageRowBounds;
import org.event.mapper.CategoryMapper;
import org.event.mapper.UserMapper;
import org.event.mapper.UserRoleMapper;
import org.event.pojo.Article;
import org.event.pojo.PageBean;
import org.event.pojo.User;
import org.event.pojo.UserRole;
import org.event.pojo.dao.UserRoleDao;
import org.event.service.UserRoleService;
import org.event.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public PageBean<UserRoleDao> list(Integer pageNum, Integer pageSize, Integer roleId, String nickname) {
        //创建pageBean
        PageBean<UserRoleDao> pageBean=new PageBean<>();
        PageHelper.startPage(pageNum,pageSize);

        Page<UserRoleDao> page =userRoleMapper.list(roleId,nickname);
        List<UserRoleDao> list = page.getResult();
        for (UserRoleDao userRoleDao:list){
            User user = userMapper.findByUserId(userRoleDao.getUserId());
            userRoleDao.setUpdateUserName(user.getNickname());
        }
        Long total=page.getTotal();
        pageBean.setTotal(total);
        pageBean.setItems(list);

        return pageBean;
    }

    @Override
    public void update(UserRole userRole) {
        if (userRole.getUserId().equals(ThreadLocalUtil.getUserId())){
            throw new RuntimeException("不能修改自己的角色");
        }
        userRole.setUpdateTime(LocalDateTime.now());
        userRole.setUpdateUser(ThreadLocalUtil.getUserId());
        userRoleMapper.update(userRole);
    }

    @Override
    public void delete(Integer userId, Integer roleId) {
        if (categoryMapper.findByUserId(userId)!=null){
            throw new RuntimeException("该用户下有分类，无法删除用户角色");
        }
        userRoleMapper.deleteByUserIdAndRoleId(userId,roleId);
    }

    @Override
    public void add(UserRole userRole) {
        if (userRoleMapper.getByUserId(userRole.getUserId())!=null){
            throw new RuntimeException("该用户下已有该角色");
        }
        userRole.setCreateTime(LocalDateTime.now());
        userRole.setUpdateTime(LocalDateTime.now());
        userRole.setUpdateUser(ThreadLocalUtil.getUserId());

        userRoleMapper.add(userRole);
    }


}
