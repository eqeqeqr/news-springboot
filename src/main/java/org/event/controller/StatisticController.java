package org.event.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.checkerframework.checker.units.qual.A;
import org.event.anno.Menu;
import org.event.pojo.Result;
import org.event.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/statistic")
//@Menu(menuName = "数据统计")
public class StatisticController {
    @Autowired
    private ArticleService articleService;
    @GetMapping("/statisticsBar")
    @Menu(menuName = "文章统计")
    public Result<Map<String, List>> statisticsBar(
            @RequestParam(required = true)  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime
    ) {
        if (startTime.isAfter(endTime)){
            throw new RuntimeException("开始时间不能大于结束时间");
        }
        Map<String,List> map = articleService.statistics( startTime, endTime);
        return Result.success(map);
    }
    @Menu(menuName = "文章统计")
    @GetMapping("/statisticsLine")
    public Result<Map<String, List>> statisticsLine(
            @RequestParam(required = true)  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime
    ) {
        if (startTime.isAfter(endTime)){
            throw new RuntimeException("开始时间不能大于结束时间");
        }
        Map<String,List> map = articleService.statisticsLine( startTime, endTime);
        return Result.success(map);
    }
    @Menu(menuName = "文章统计")
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        articleService.exportBusinessData(response);
    }
}
