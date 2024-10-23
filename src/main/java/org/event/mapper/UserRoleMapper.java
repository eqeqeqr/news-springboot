package org.event.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;
import org.event.pojo.UserRole;
import org.event.pojo.dao.UserRoleDao;

import java.util.List;

@Mapper
public interface UserRoleMapper {
    @Select("select * from user_role where user_id = #{userId}")
    UserRole getByUserId(Integer userId);

    @Insert("insert into user_role(user_id,role_id,create_time,update_time) values(#{userId},#{roleId},#{createTime},#{updateTime})")
    void add(UserRole userRole);

    Page<UserRoleDao> list(Integer roleId, String nickname);

    @Update("update user_role set update_time = #{updateTime},update_user=#{updateUser},role_id=#{roleId} where user_id = #{userId} and role_id=#{oldRoleId}")
    void update(UserRole userRole);

    @Delete("delete from user_role where user_id = #{userId} and role_id=#{roleId}")
    void deleteByUserIdAndRoleId(Integer userId, Integer roleId);
    @Select("select * from user_role where user_id = #{userId} and role_id=#{roleId}")
    UserRole getByUserIdAndRoleId(Integer userId, Integer roleId);

    @Select("select * from user_role where role_id = #{roleId}")
    List<UserRole> getByRoleId(Integer roleId);
}
