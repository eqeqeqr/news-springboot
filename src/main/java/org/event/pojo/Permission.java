package org.event.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    private Integer id;
    private String name;
    private LocalDateTime createTime;//创建时间
    private Integer createUser;
    private String url;
    private Integer parentId;
    private String icon;
    //private LocalDateTime updateTime;//更新时间
}
