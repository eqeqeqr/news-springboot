package org.event.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;
import org.event.pojo.Category;

import java.util.List;

@Mapper
public interface CategoryMapper {
    @Insert("insert into category(category_alias,category_name,create_time,create_user,update_time) " +
            "values (#{categoryAlias},#{categoryName},#{createTime},#{createUser},#{updateTime})")
    void add(Category category);
    @Select("select * from category ")
    List<Category> list();
    Page<Category> pageList(String categoryName);
    @Select("select * from category where id = #{id}")
    Category findById(Integer id);


    void update(Category category);

    @Delete("delete from category where id = #{id}")
    void deleteById(Integer id);

    @Select("select * from category where create_user = #{userId}")
    Category findByUserId(Integer userId);
}
