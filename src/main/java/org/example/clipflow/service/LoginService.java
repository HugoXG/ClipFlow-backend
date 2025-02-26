package org.example.clipflow.service;

import jakarta.servlet.http.HttpServletResponse;
import org.example.clipflow.entity.Captcha;
import org.example.clipflow.entity.user.User;
import org.example.clipflow.entity.vo.FindPWVO;
import org.example.clipflow.entity.vo.RegisterVO;

import java.io.IOException;


public interface LoginService {
    /**
     * 获取图形验证码
     * @param response 响应数据
     * @param uuid uuid
     * @throws IOException IO错误
     */
    void captcha(HttpServletResponse response, String uuid) throws IOException;

    /**
     * 获取邮箱验证码
     * @param captcha 图形验证码类
     * @return 布尔
     */
    boolean getEmailCode(Captcha captcha);

    /**
     * 验证邮箱验证码
     * @param email 邮箱
     * @param code 验证码
     * @return 布尔
     */
    boolean checkEmailCode(String email, Integer code);

    /**
     * 注册
     * @param registerVO VO
     * @return 布尔
     */
    boolean register(RegisterVO registerVO);

    /**
     * 更新密码
     * @param findPWVO VO
     * @return 布尔
     */
    boolean updatePassword(FindPWVO findPWVO);

    /**
     * 登录
     * @param user 账户
     * @return User
     */
    User login(User user);
}
