package org.event.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.event.pojo.Permission;
import org.event.pojo.RolePermission;
import org.event.pojo.dao.RolePermissionDao;

import java.util.List;

@Mapper
public interface RolePermissionMapper {


    @Select("select * from role_permission where role_id = #{roleId}")
    List<RolePermission> getByRoleId(Integer roleId);


    Page<RolePermissionDao> list(Integer roleId, Integer permissionId);

    @Select("select * from role_permission where role_id = #{roleId} and permission_id = #{permissionId}")
    RolePermission getByRoleIdAndPermissionId(Integer roleId, Integer permissionId);

    void add(RolePermission rolePermission);

    @Delete("delete from role_permission where role_id = #{roleId} and permission_id = #{permissionId}")
    void deleteByRoleIdAndPermissionId(Integer roleId, Integer permissionId);

    @Select("select * from role_permission where permission_id = #{permissionId}")
    List<RolePermission> getByPermissionId(Integer permissionId);
}
