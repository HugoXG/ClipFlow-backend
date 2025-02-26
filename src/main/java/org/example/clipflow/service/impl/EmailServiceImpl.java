package org.example.clipflow.service.impl;

import org.example.clipflow.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    private final SimpleMailMessage simpleMailMessage;
    public EmailServiceImpl(JavaMailSender javaMailSender,
                            SimpleMailMessage simpleMailMessage) {
        this.javaMailSender = javaMailSender;
        this.simpleMailMessage = simpleMailMessage;
    }

    @Value("${spring.mail.username}")
    private String fromName;
    @Override
    @Async
    public void send(String email, String context) {
        simpleMailMessage.setSubject("模仿-幸运日");
        simpleMailMessage.setFrom(fromName);
        simpleMailMessage.setText(context);
        simpleMailMessage.setTo(email);
        javaMailSender.send(simpleMailMessage); //final todo 手动抛出错误
    }
}
