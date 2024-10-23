package org.event.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.event.anno.Menu;
import org.event.pojo.Article;
import org.event.pojo.PageBean;
import org.event.pojo.Result;
import org.event.pojo.dao.PermissionDao;
import org.event.service.ArticleService;
import org.event.utils.JwtUtil;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Format;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/article")
//@Menu(menuName = "文章管理")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/list")
    public Result list() {
            return Result.success("文章显示。。。。。。。。。。");
    }
    @PostMapping
    @Menu(menuName = "文章展示")
    public Result add(@RequestBody @Validated(Article.Add.class) Article article) {
        articleService.add(article);
        return Result.success();

    }
    @Menu(menuName = "文章展示")
    @GetMapping
    public Result<PageBean<Article>> list(Integer pageNum,
                                          Integer pageSize,
                                          @RequestParam(required = false) Integer categoryId,
                                          @RequestParam(required = false) String state) {
        PageBean<Article> pageBean = articleService.list(pageNum,pageSize,categoryId,state);
        return Result.success(pageBean);
    }

    @GetMapping("/detail")
    @Menu(menuName = "文章展示")
    public Result<Article> detail(@RequestParam(required = true)  Integer id) {
        Article article = articleService.detail(id);
        return Result.success(article);
    }
    @DeleteMapping
    @Menu(menuName = "文章展示")
    public Result delete(@RequestParam(required = true)  Integer id) {
        articleService.deleteById(id);
        return Result.success();
    }
    @PutMapping
    @Menu(menuName = "文章展示")
    public Result update(@RequestBody @Validated(Article.Update.class) Article article) {
        articleService.update(article);
        return Result.success();
    }

}
