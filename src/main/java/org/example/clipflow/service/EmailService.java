package org.example.clipflow.service;

public interface EmailService {
    /**
     * 发送邮箱验证码
     * @param email 邮箱地址
     * @param context 邮箱验证码
     */
    void send(String email, String context);
}
