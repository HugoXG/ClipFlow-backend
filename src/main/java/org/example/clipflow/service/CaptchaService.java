package org.example.clipflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.clipflow.entity.Captcha;

import java.awt.image.BufferedImage;

public interface CaptchaService extends IService<Captcha> {
    /**
     * 获取图新验证码
     * @param uuId uuid
     * @return 图片
     */
    BufferedImage getCaptcha(String uuId);

    /**
     * 验证码图形验证码
     * @param captcha 图形验证码
     * @return 布尔
     */
    boolean validate(Captcha captcha);
}
