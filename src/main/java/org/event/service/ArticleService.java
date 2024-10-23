package org.event.service;

import jakarta.servlet.http.HttpServletResponse;
import org.event.pojo.Article;
import org.event.pojo.PageBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ArticleService {
    void add(Article article);



    PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state);

    Article detail(Integer id);

    void deleteById(Integer id);

    void update(Article article);


    Map<String, List> statistics(LocalDateTime startTime, LocalDateTime endTime);

    Map<String, List> statisticsLine(LocalDateTime startTime, LocalDateTime endTime);

    void exportBusinessData(HttpServletResponse response);

    PageBean<Article> Indexlist(Integer pageNum, Integer pageSize, String searchContent, Integer categoryId);
}
