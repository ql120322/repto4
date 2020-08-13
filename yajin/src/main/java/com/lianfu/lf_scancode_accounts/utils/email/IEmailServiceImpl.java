package com.lianfu.lf_scancode_accounts.utils.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class IEmailServiceImpl implements IEmailService {


    //从application.properties配置文件中注入发送者的邮件地址
    @Value("${spring.mail.username}")
    private String fromEmail;

    //注入spring发送邮件的对象
    @Autowired
    private JavaMailSender jms;

    @Override
    public void sendAttachmentMail(String to, String subject, String content, String filePath, String fileName) {

        MimeMessage message = jms.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content);
            //验证文件数据是否为空
            if (null != filePath) {
                FileSystemResource file = null;
                //添加附件
                file = new FileSystemResource(filePath);
                helper.addAttachment(fileName + ".pdf", file);
            }
            jms.send(message);
            System.out.println("带附件的邮件发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("发送带附件的邮件失败");
        }

    }

}

