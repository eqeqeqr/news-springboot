package org.event.pojo.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.event.pojo.Article;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDao extends Article {
    private String categoryName;
    private String createUserName;
}
