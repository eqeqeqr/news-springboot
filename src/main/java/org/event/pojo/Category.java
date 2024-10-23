package org.event.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class Category {
    @NotNull(groups = {Update.class})//指定了分组
    private Integer id;//主键ID
    @NotEmpty//属于默认分组
    private String categoryName;//分类名称
    @NotEmpty//属于默认分组
    private String categoryAlias;//分类别名
    private Integer createUser;//创建人ID
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;//创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;//更新时间

    //如果某个校验项没有指定分组，默认属于Default分组
    //分组之间可以继承，A extend B 那么A中拥有B中所有的校验项
    public interface Add extends Default{}
    public interface Update extends Default {}
}
