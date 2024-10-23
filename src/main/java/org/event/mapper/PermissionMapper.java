package org.event.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.event.pojo.Permission;
import org.event.pojo.Role;
import org.event.pojo.UserRole;
import org.event.pojo.dao.PermissionDao;
import org.event.pojo.dao.PermissionShowDao;

import java.util.List;

@Mapper
public interface PermissionMapper {
    @Select("select p.*,u.nickname as createUserName from permission p LEFT JOIN user u ON p.create_user = u.id where p.id = #{permissionId}")
    PermissionDao getByRoleId(Integer permissionId);

    @Select("select * from permission group by id")
    List<PermissionDao> list();

    Page<PermissionDao> pageList(String name);


    void add(Permission permission);

    @Delete("delete from permission where id = #{id}")
    void deleteById(Integer id);

    @Select("select * from permission where name = #{name}")
    Permission getByName();

    @Select("select * from permission where id = #{parentId}")
    Permission getById(Integer parentId);
}
