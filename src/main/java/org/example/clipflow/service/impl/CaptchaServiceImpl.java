package org.example.clipflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.example.clipflow.constant.RedisConstant;
import org.example.clipflow.entity.Captcha;
import org.example.clipflow.exception.BaseException;
import org.example.clipflow.mapper.CaptchaMapper;
import org.example.clipflow.service.CaptchaService;
import org.example.clipflow.service.EmailService;
import org.example.clipflow.utils.DateUtil;
import org.example.clipflow.utils.RedisCaptchaUtil;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.Date;

@Slf4j
@Service
public class CaptchaServiceImpl extends ServiceImpl<CaptchaMapper, Captcha> implements CaptchaService {
    private final Producer producer;
    private final EmailService emailService;
    private final RedisCaptchaUtil redisCaptchaUtil;
    public CaptchaServiceImpl(Producer producer,
                              EmailService emailService,
                              RedisCaptchaUtil redisCaptchaUtil) {
        this.producer = producer;
        this.emailService = emailService;
        this.redisCaptchaUtil = redisCaptchaUtil;
    }
    @Override
    public BufferedImage getCaptcha(String uuId) {
        // 调用google依赖kaptcha生成验证码
        Captcha captcha = new Captcha();
        String code = producer.createText();
        captcha.setUuid(uuId);
        captcha.setCode(code);
        captcha.setExpireTime(DateUtil.addDateMinutes(new Date(), 5));
        this.save(captcha); //final todo 后续替换为redis存储图片验证码
        return producer.createImage(code);
    }

    @Override
    public boolean validate(Captcha captcha) {
        LambdaQueryWrapper<Captcha> queryWrapper = new LambdaQueryWrapper<>();
        Captcha captchaPO = this.getOne(queryWrapper.eq(Captcha::getUuid, captcha.getUuid()));
        if (captchaPO == null) {
            throw new BaseException("uuid错误，查询图形验证码失败");
        }

        this.remove(queryWrapper.eq(Captcha::getUuid, captchaPO.getUuid())); //final todo 后续替换为redis删除存储图片验证码
        if (captchaPO.getExpireTime().getTime() <= System.currentTimeMillis()) {
            throw new BaseException("uuid已过期");
        }
        if (!captcha.getCode().equals(captchaPO.getCode())) {
            throw new BaseException("code错误");
        }

        String emailCode = getSixCode();
        redisCaptchaUtil.set(RedisConstant.EMAIL_CODE + captcha.getEmail(), emailCode, RedisConstant.EMAIL_CODE_TIME);
        emailService.send(captcha.getEmail(), "注册验证码："+emailCode+"，验证码5分钟内有效。");
        return true;
    }

    /**
     * 获取六位验证码
     * @return 六位验证码
     */
    private static String getSixCode() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int code = (int) (Math.random() * 10);
            stringBuilder.append(code);
        }
        return stringBuilder.toString();
    }
}
