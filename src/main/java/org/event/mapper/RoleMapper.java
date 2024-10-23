package org.event.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.event.pojo.Role;
import org.event.pojo.dao.RoleDao;

import java.util.List;

@Mapper
public interface RoleMapper {
    @Select("select * from role")
    public List<Role> list() ;

    Page<RoleDao> pageList(String roleName, Integer roleWeight);

    void add(Role role);

    @Select("select name from role where name = #{name}")
    Role getByName(String name);

    @Delete("delete from role where id = #{id}")
    void deleteById(Integer id);

    void update(Role role);

    @Select("select * from role where id = #{id}")
    Role getById(Integer id);
}
