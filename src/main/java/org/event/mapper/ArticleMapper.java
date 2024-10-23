package org.event.mapper;

import com.github.pagehelper.Page;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.annotations.*;
import org.event.pojo.Article;
import org.event.pojo.dao.ArticleDao;
import org.event.pojo.dao.ArticleStatisticsDao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface ArticleMapper {
    @Insert("insert into article(category_id,content,cover_img,create_time,create_user,state,title,update_time) " +
            "values(#{categoryId},#{content},#{coverImg},#{createTime},#{createUser},#{state},#{title},#{updateTime})")
    void add(Article article);

    Page<Article> list(Integer userId, Integer categoryId, String state,Integer weight,String searchContent);

    @Select("SELECT a.*,ur.role_id ,r.`name` as roleName,u.nickname as createUserName ,r.weight FROM `article` a LEFT JOIN user_role ur on a.create_user=ur.user_id LEFT JOIN role r on ur.role_id=r.id left JOIN user u on a.create_user=u.id where a.id=#{id}")
    Article detail(Integer id);

    @Delete("delete from article where id=#{id}")
    void deleteById(Integer id);

    @Update("update article set category_id=#{categoryId},content=#{content},cover_img=#{coverImg},state=#{state},title=#{title},update_time=#{updateTime} where id=#{id}")
    void update(Article article);

    @Select("SELECT r.`name` as roleName, count(ur.role_id) as articleNumber FROM `article` a LEFT JOIN user_role ur ON a.create_user=ur.user_id LEFT JOIN role r ON r.id=ur.role_id WHERE a.create_time BETWEEN #{startTime} AND #{endTime} GROUP BY ur.role_id")
    List<ArticleStatisticsDao> articleCount(LocalDateTime startTime, LocalDateTime endTime);

    @Select("SELECT count(*) FROM article  WHERE create_time BETWEEN #{startTime} AND #{endTime} ")
    int articleNumber( LocalDateTime startTime, LocalDateTime endTime);


    @Select("select a.*,u.nickname as createUserName,c.category_name as categoryName from article a LEFT JOIN user u ON a.create_user=u.id LEFT JOIN category c ON a.category_id = c.id WHERE a.create_time BETWEEN #{startTime} and #{endTime}")
    List<ArticleDao> Data30List( LocalDateTime startTime, LocalDateTime endTime);
}
