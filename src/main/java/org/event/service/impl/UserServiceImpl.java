package org.event.service.impl;

import org.event.mapper.RolePermissionMapper;
import org.event.mapper.UserMapper;
import org.event.mapper.UserRoleMapper;
import org.event.pojo.User;
import org.event.pojo.UserRole;
import org.event.service.UserService;
import org.event.utils.Md5Util;
import org.event.utils.ThreadLocalUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Override
    public User findByUserName(String username) {
        User user=userMapper.findByUserName(username);
        return user;
    }

    @Override
    public void register(String username, String password,String email) {
        //加密
        String md5String = Md5Util.getMD5String(password);
        //添加
        LocalDateTime nowTime=LocalDateTime.now();
        User user=User.builder()
                .email(email)
                .username(username)
                .password(md5String)
                .createTime(nowTime)
                .updateTime(nowTime)
                .build();
        userMapper.add(user);

        UserRole userRole=new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(1);
        userRole.setCreateTime(nowTime);
        userRole.setUpdateTime(nowTime);
        userRoleMapper.add(userRole);

    }

    @Override
    public void update(User user) {
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
    }

    @Override
    public void updateAvatar(String avatarUrl) {
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer id=(Integer) map.get("id");
        User user=User.builder()
                .id(id)
                .userPic(avatarUrl)
                .updateTime(LocalDateTime.now())
                .build();
        userMapper.update(user);
    }

    @Override
    public void updatePwd(User user, String newPassword) {
        User newUser=new User();
        BeanUtils.copyProperties(user,newUser);
        newUser.setPassword(Md5Util.getMD5String(newPassword));
        newUser.setUpdateTime(LocalDateTime.now());
        System.out.println("更新加密后的密码为"+Md5Util.getMD5String(newPassword));
        userMapper.update(newUser);
    }

    @Override
    public List<User> list() {
        List<User> list=userMapper.list();
        return list;
    }
}
