package org.event.mapper;

import org.apache.ibatis.annotations.*;
import org.event.pojo.User;

import java.util.List;

@Mapper
public interface UserMapper {
    /*
    *
    *
    *  private Integer id;//主键ID
       username;//用户名
       password;//密码
       nickname;//昵称
       email;//邮箱
       userPic;//用户头像地址
       createTime;//创建时间
    *  updateTime;//更新时间
    * */
    //添加
    @Insert("insert into user(username,password,nickname,email,user_pic,create_time,update_time) " +
            "values(#{username},#{password},#{nickname},#{email},#{userPic},#{createTime},#{updateTime})"
    )
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(User user);
    //根据用户名查询用户
    @Select("SELECT u.*,ur.role_id,r.`name` as roleName FROM `user` u LEFT JOIN user_role ur on u.id=ur.user_id LEFT JOIN role r ON ur.role_id=r.id where username = #{username}")
    User findByUserName(String username);

    @Select("select * from user where id = #{userId}")
    User findByUserId(Integer userId);

    void update(User user);
    @Select("select * from user")
    List<User> list();
}
