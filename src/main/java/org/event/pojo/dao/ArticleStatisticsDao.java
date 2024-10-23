package org.event.pojo.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleStatisticsDao {
    private String roleName;
    private Integer articleNumber;
}
