package org.event.utils;

import com.baidubce.qianfan.Qianfan;
import com.baidubce.qianfan.core.builder.ChatBuilder;
import com.baidubce.qianfan.model.chat.ChatResponse;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
@Data
public class BaiduQianFanUtil {

    @Value("${baidu.qianfan.access-key}")
    String QIANFAN_ACCESS_KEY;
    @Value("${baidu.qianfan.secret-key}")
    String QIANFAN_SECRET_KEY;
   /* @Value("")
    String QIANFAN_ENDPOINT;*/
    @Value("${baidu.qianfan.model}")
    String QIANFAN_MODEL;
    @Value("${baidu.qianfan.temperature}")
    double QIANFAN_TEMPERATURE;
    public  static ConcurrentHashMap<String,ChatBuilder> chatBuilderMap=new ConcurrentHashMap<>();
    Qianfan qianfan;
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    @PostConstruct
    public void init() {
        this.qianfan = new Qianfan(QIANFAN_ACCESS_KEY, QIANFAN_SECRET_KEY);
    }
    public ChatBuilder getChatBuilder(String key) {
        if (chatBuilderMap.containsKey(key)){
            return chatBuilderMap.get(key);
        }else {

            ChatBuilder chatBuilder = this.qianfan.chatCompletion().model(QIANFAN_MODEL).temperature(QIANFAN_TEMPERATURE).system("你是新闻AI助手");
            chatBuilderMap.put(key,chatBuilder);
            return chatBuilder;
        }

    }
    public Future<String> sendMessage(String builderKey, String question) {
        return executorService.submit(()->{
            ChatBuilder chatBuilder = getChatBuilder(builderKey);
            ChatResponse response = chatBuilder
                    // 添加用户的第一轮消息
                    .addUserMessage(question)
                    .execute(); // 发起请求
            String result=response.getResult().replaceAll("文心一言","新闻助手").replaceAll("ERNIE Bot","News Assistant");
            chatBuilder.systemMemoryId(response.getId()).addAssistantMessage(response.getResult());
            return result;
        });
    }
    public void removeChatBuilder(String key) {
        chatBuilderMap.remove(key);
    }

}
