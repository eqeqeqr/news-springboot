package org.event.controller;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.experimental.PackagePrivate;
import org.event.anno.Menu;
import org.event.pojo.Result;
import org.event.pojo.User;
import org.event.pojo.dao.EmailDao;
import org.event.service.UserService;
import org.event.utils.EmailUtil;
import org.event.utils.JwtUtil;
import org.event.utils.Md5Util;
import org.event.utils.ThreadLocalUtil;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated
//@Menu(menuName = "个人中心")
//@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private EmailDao emailDao;
    @PostMapping("/sendEmail")
    public Result sendEmail(@Email String email) {
        if (!StringUtils.hasText(email)){
            return Result.error("邮箱不能为空");
        }
        String code=EmailUtil.generateCode(6);
        String content = EmailUtil.generateVerifyHtml(code);
        String subject="验证码";
        boolean flag=emailUtil.sendEmail(emailDao.getSenderEmail(),email,emailDao.getSender(),subject,content);
        if (!flag){
            return Result.error("发送失败");
        }
        String key="verify:"+email;
        stringRedisTemplate.opsForValue().set(key,code,5, TimeUnit.MINUTES);
        return Result.success();
    }
    @PostMapping("/register")
    //请求参数格式：x-www-form-urlencoded
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username,@Pattern(regexp = "^\\S{5,16}$")  String password,@Email String email,@NotNull  String code) {
        //查询用户
        User user=userService.findByUserName(username);
        if (user!=null){
            return Result.error("用户已存在");
        }
        if (!code.equals(stringRedisTemplate.opsForValue().get("verify:"+email))){
            return Result.error("验证码错误");
        }
        //注册
        userService.register(username,password,email);
        stringRedisTemplate.delete("verify:"+email);
        return Result.success();
    }

    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$") String username,@Pattern(regexp = "^\\S{5,16}$")  String password){
        //根据用户名查询用户
        User user=userService.findByUserName(username);
        //判断用户是否存在
        if (user==null){
            return Result.error("用户不存在");
        }
        //判断密码是否正确
        if (Md5Util.getMD5String(password).equals(user.getPassword())){
            Map<String,Object> claims=new HashMap<>();
            claims.put("id",user.getId());
            claims.put("username",user.getUsername());
            String token = JwtUtil.genToken(claims);
            //把token存储到redis中
            ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
            valueOperations.set(user.getId()+":"+user.getUsername(),token,12, TimeUnit.HOURS);
            return Result.success(token);
        }
        return Result.error("密码错误");
    }
    @GetMapping("/userInfo")
    @Menu(menuName = "基本资料")
    public Result<User> userInfo(){

        Map<String,Object> map= ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User user=userService.findByUserName(username);
        return Result.success(user);
    }
    @PutMapping("/update")
    @Menu(menuName = "基本资料")
    //：application/json
    public Result update(@RequestBody @Validated User user){
        userService.update(user);
        return Result.success();
    }
    @PatchMapping("/updateAvatar")
    //queryString
    @Menu(menuName = "更换头像")
    public Result updateAvatar(@RequestParam @URL String avatarUrl){

        userService.updateAvatar(avatarUrl);
        return Result.success();
    }
    @PatchMapping("/forgetPwd")
    public Result verifyCode(@RequestBody Map<String,String>params){
        Integer step= Integer.valueOf(params.get("step"));
        String newPassword = params.get("new_pwd");
        String repeatPassword = params.get("re_pwd");
        String code = params.get("code");
        String username = params.get("username");
        String email = params.get("email");


        switch (step)
        {
            //1.查找用户是否存在
            case 1:{
                if (!StringUtils.hasText(username)){
                    return Result.error("参数错误");
                }
                User user=userService.findByUserName(username);
                if (user==null){
                    return Result.error("用户不存在");
                }
                break;
            }
            //2.校验验证码
            case 2:{
                if (!(StringUtils.hasText(email)||!StringUtils.hasText(code))){
                    return Result.error("参数错误");
                }
                User user=userService.findByUserName(username);
                if (!user.getEmail().equals(email)){
                    return Result.error("邮箱错误");
                }
                String key="verify:"+email;
                String redisCode=stringRedisTemplate.opsForValue().get(key);

                //2.删除验证码
                stringRedisTemplate.delete(key);
                if (!code.equals(redisCode)){
                    return Result.error("验证码错误");
                }
                break;
            }
            case 3:{
                User user=userService.findByUserName(username);
                if (!(StringUtils.hasText(newPassword)||StringUtils.hasText(repeatPassword))){
                    return Result.error("参数错误");
                }
                if (!newPassword.equals(repeatPassword)){
                    return Result.error("两次密码不一致");
                }
                System.out.println("newPassword = " + newPassword+"------"+repeatPassword);
                userService.updatePwd(user,newPassword);
                break;
            }
            default:{
                return Result.error("未知错误");
            }


        }




        return Result.success();
    }
    @PatchMapping("/updatePwd")
    @Menu(menuName = "重置密码")
    public Result updatePassword(@RequestBody Map<String,String>params){
        //1.校验参数
        String oldPassword = params.get("old_pwd");
        String newPassword = params.get("new_pwd");
        String repeatPassword = params.get("re_pwd");
        if (!(StringUtils.hasText(oldPassword)||StringUtils.hasText(newPassword)||StringUtils.hasText(repeatPassword))){
            return Result.error("参数错误");
        }
        //原密码是否正确
        //2.查询用户
        Map<String,Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User user = userService.findByUserName(username);
        if (user==null){
            return Result.error("用户不存在");
        }
        oldPassword=Md5Util.getMD5String(oldPassword);
        if (!oldPassword.equals(user.getPassword())){
            return Result.error("原密码错误");
        }
        //3.更新密码
        if (!newPassword.equals(repeatPassword)){
            return Result.error("两次密码不一致");
        }

        userService.updatePwd(user,newPassword);
        //删除redis中的token
        stringRedisTemplate.delete(user.getId()+":"+user.getUsername());
        return Result.success();
    }
    @GetMapping("/list")
    public Result<List<User>> list(){
        List<User> list=userService.list();
        return Result.success(list);
    }
}
