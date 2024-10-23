package org.event.utils;

import jakarta.mail.internet.MimeMessage;
import org.event.pojo.dao.EmailDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class EmailUtil {
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    private EmailDao emailDao;
    public boolean   sendEmail(String senderEmail,String targetEmail,String sender,String subject,String content){
        // 创建一个邮件消息
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            // 创建 MimeMessageHelper
            MimeMessageHelper helper = new MimeMessageHelper(message, false);

            // 发件人邮箱和名称
            helper.setFrom(senderEmail, sender);
            // 收件人邮箱
            helper.setTo(targetEmail);
            // 邮件标题
            helper.setSubject(subject);
            // 邮件正文，第二个参数表示是否是HTML正文
            helper.setText(content, true);

            // 发送
            javaMailSender.send(message);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
    public static String generateCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            code.append(characters.charAt(index));
        }
        return code.toString();
    }
    //邮箱验证码html模板
    public static String generateVerifyHtml(String code) {
        String content = "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f4f4f4; }" +
                ".container { max-width: 600px; margin: auto; padding: 20px; background: #fff; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }" +
                ".header { font-size: 24px; font-weight: bold; color: #333; margin-bottom: 20px; }" +
                ".content { font-size: 16px; color: #555; }" +
                ".code { font-size: 24px; font-weight: bold; color: #007BFF; }" +
                ".footer { margin-top: 20px; font-size: 14px; color: #888; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>验证码</div>" +
                "<div class='content'>" +
                "<p>您好!</p>" +
                "<p>您的验证码是: <span class='code'>" + code + "</span></p>" +
                "</div>" +
                "<div class='footer'>感谢您使用我们的服务。如果您有任何问题，请随时与我们联系。1500292505@qq.com</div>" +
                "</div>" +
                "</body>" +
                "</html>";
        return content;
    }
}
