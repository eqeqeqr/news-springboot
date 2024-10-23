package org.event.controller;

import org.checkerframework.checker.units.qual.A;
import org.event.pojo.Result;
import org.event.utils.BaiduQianFanUtil;
import org.event.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


@RestController
@RequestMapping("/ai")
public class ChatController {
    @Autowired
    BaiduQianFanUtil baiduQianFanUtil;

    @GetMapping("/openAI")
    public Result help(@RequestParam(required = false,defaultValue = "你好") String question){


    //    question="您是一名新闻助手，只会回答有关新闻主题的内容，拒绝回答其它非新闻的内容，你会根据用户的主要新闻内容，帮他完成主要新闻内容的优化和编辑"+"\n主要新闻内容为:"+ question;

        Future<String> future = baiduQianFanUtil.sendMessage(ThreadLocalUtil.getUserId() + ThreadLocalUtil.getUserName(), question);
         try {
            String result= future.get();
            return Result.success(result);
        }catch (Exception e){
            return Result.error(e.getMessage());
        }

    }
    @GetMapping("/closeAI")
    public Result closeAI(){
        baiduQianFanUtil.removeChatBuilder(ThreadLocalUtil.getUserId() + ThreadLocalUtil.getUserName());
        return Result.success();
    }
}
