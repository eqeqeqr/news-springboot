package org.event.controller;

import org.event.pojo.Article;
import org.event.pojo.Category;
import org.event.pojo.PageBean;
import org.event.pojo.Result;
import org.event.service.ArticleService;
import org.event.service.CategotyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/index")
public class IndexController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CategotyService categotyService;
    @Caching(cacheable = {
            @Cacheable(cacheNames = "indexCache",key ="'article:' + #root.methodName + ':' + #pageNum + ':' + #pageSize + ':' + #searchContent + ':' + #categoryId" ,cacheManager = "caffeineCacheManager"),
        //    @Cacheable(cacheNames = "indexCache",key ="'article:' + #root.methodName + ':' + #pageNum + ':' + #pageSize + ':' + #searchContent + ':' + #categoryId", cacheManager = "redisCacheManager")
    })
    @GetMapping("/article")
    public Result<PageBean<Article>> list(Integer pageNum,
                                          Integer pageSize,
                                          @RequestParam(required = false) String searchContent,
                                          @RequestParam(required = false) Integer categoryId) {
        PageBean<Article> pageBean = articleService.Indexlist(pageNum,pageSize,searchContent,categoryId);
        return Result.success(pageBean);
    }
    @Caching(cacheable = {
            @Cacheable(cacheNames = "indexCache",key ="'category:'+#root.methodName" ,cacheManager = "caffeineCacheManager"),
       //     @Cacheable(cacheNames = "indexCache", key ="'category:'+#root.methodName",cacheManager = "redisCacheManager")
    })
    @GetMapping("/category")
    public Result<List<Category>> list() {
        List<Category>list=categotyService.list();
        return Result.success(list);
    }
    @Caching(cacheable = {
            @Cacheable(cacheNames = "indexCache", key ="#root.methodName+':article:'+#id", cacheManager = "caffeineCacheManager"),
         //   @Cacheable(cacheNames = "indexCache", key ="#root.methodName+':article:'+#id",cacheManager = "redisCacheManager")
    })
    @GetMapping("/detail/{id}")
    public Result<Article> detail(@PathVariable Integer id) {

        Article article = articleService.detail(id);
        return Result.success(article);
    }
}
