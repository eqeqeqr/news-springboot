package org.event.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;
import lombok.Data;
import org.event.anno.State;
import org.hibernate.validator.constraints.URL;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Format;

import java.time.LocalDateTime;

@Data
public class Article {
    @NotNull(groups = {Update.class})
    private Integer id;//主键ID
    @NotEmpty
    @Pattern(regexp = "^[\\s\\S]{1,50}$", message = "标题必须包含1到50个字符")
    private String title;//文章标题
    @NotEmpty
    private String content;//文章内容
    @NotEmpty
    @URL
    private String coverImg;//封面图像
    @State
    private String state;//发布状态 已发布|草稿
    @NotNull
    private Integer categoryId;//文章分类id
    private Integer createUser;//创建人ID
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;//创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;//更新时间
    private String roleName;
    private String createUserName;
    public interface Add extends Default {}
    public interface Update extends Default {}
}
