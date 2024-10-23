package org.event.service;

import org.event.pojo.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {
    //根据用户名查询用户
    User findByUserName(String username);
    //注册
    void register(String username, String password,String email);

    void update(User user);

    void updateAvatar(String avatarUrl);

    void updatePwd(User user, String newPassword);

    List<User> list();
}
