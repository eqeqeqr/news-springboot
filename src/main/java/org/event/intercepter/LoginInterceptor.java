package org.event.intercepter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.event.pojo.Result;
import org.event.utils.JwtUtil;
import org.event.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token=request.getHeader("Authorization");
        //验证token

        try {

            Map<String, Object> claims = JwtUtil.parseToken(token);
            //从redis中获取token
            ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
            String redisToken = valueOperations.get(claims.get("id") + ":" + claims.get("username"));
            if (redisToken == null || !redisToken.equals(token)){
                System.out.println("token验证失败");
                throw new RuntimeException("token验证失败");
            }
            //业务数据存储到ThreadLocal中
            ThreadLocalUtil.set(claims);
            System.out.println("设置解析后的token数据放入ThreadLocal中的数据");
            System.out.println(request.getRequestURI());

            return true;//放行
        }catch (Exception e){
            response.setStatus(401);
            return false;//不放行

        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //清空ThreadLocal中的数据
        System.out.println("清空ThreadLocal中的数据");
        ThreadLocalUtil.remove();
    }
}
