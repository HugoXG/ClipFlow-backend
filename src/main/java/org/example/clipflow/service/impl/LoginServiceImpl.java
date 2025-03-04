package org.example.clipflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.example.clipflow.constant.RedisConstant;
import org.example.clipflow.entity.Captcha;
import org.example.clipflow.entity.user.User;
import org.example.clipflow.entity.vo.FindPWVO;
import org.example.clipflow.entity.vo.RegisterVO;
import org.example.clipflow.exception.BaseException;
import org.example.clipflow.service.CaptchaService;
import org.example.clipflow.service.LoginService;
import org.example.clipflow.service.user.UserService;
import org.example.clipflow.utils.EncryptionUtil;
import org.example.clipflow.utils.RedisCaptchaUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
public class LoginServiceImpl implements LoginService {
    private final CaptchaService captchaService;
    private final RedisCaptchaUtil redisCaptchaUtil;
    private final UserService userService;

    public LoginServiceImpl(CaptchaService captchaService, RedisCaptchaUtil redisCaptchaUtil, UserService userService) {
        this.captchaService = captchaService;
        this.redisCaptchaUtil = redisCaptchaUtil;
        this.userService = userService;
    }

    @Override
    public void captcha(HttpServletResponse response, String uuId) throws IOException {
        if (ObjectUtils.isEmpty(uuId)) {
            throw new IllegalArgumentException("uuId不能为空");
        }

        //控制浏览器对响应内容的缓存行为，no-store表示不允许缓存任何响应内容，no-cache表示不允许直接使用缓存
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");//设置响应的内容类型为JPEG图像

        BufferedImage bufferedImage = captchaService.getCaptcha(uuId);

        // 将图像写入响应的输出流中
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(bufferedImage, "jpeg", outputStream);
        IOUtils.closeQuietly(outputStream);
    }

    @Override
    public boolean getEmailCode(Captcha captcha) {
        return captchaService.validate(captcha);
    }

    @Override
    public boolean checkEmailCode(String email, Integer code) {
        if (ObjectUtils.isEmpty(email) || ObjectUtils.isEmpty(code)) {
            throw new BaseException("邮箱或验证码为空");
        }

        Object o = redisCaptchaUtil.get(RedisConstant.EMAIL_CODE + email);
        if (!code.toString().equals(o)) {
            throw new BaseException("验证码错误");
        }
        return true;
    }

    @Override
    public boolean register(RegisterVO registerVO) {
        return userService.register(registerVO);
    }

    @Override
    public boolean updatePassword(FindPWVO findPWVO) {
        return userService.updatePassword(findPWVO);
    }

    @Override
    public User login(User user) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getEmail, user.getEmail());
        User userPO = userService.getOne(lambdaQueryWrapper);

        if (ObjectUtils.isEmpty(userPO)) {
            throw new BaseException("账户不存在");
        }
        if (!EncryptionUtil.check(user.getPassword(), userPO.getPassword())) {
            throw new BaseException("密码错误");
        }
        return userPO;
    }
}
