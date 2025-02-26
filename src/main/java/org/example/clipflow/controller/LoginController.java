package org.example.clipflow.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.clipflow.entity.Captcha;
import org.example.clipflow.entity.user.User;
import org.example.clipflow.entity.vo.FindPWVO;
import org.example.clipflow.entity.vo.RegisterVO;
import org.example.clipflow.service.LoginService;
import org.example.clipflow.utils.JwtUtil;
import org.example.clipflow.utils.R;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/luckyjourney/login")
public class LoginController {

    //构造依赖注入
    public final LoginService loginService;
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * 登录
     * @param user 账户
     * @return JSON
     */
    @PostMapping
    public R login(@RequestBody @Validated User user) {
        log.info("接口：登录");
        user = loginService.login(user);//验证用户

        //发放token
        String token = JwtUtil.getJwtToken(user.getId(), user.getNickName());
        final HashMap<Object, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("name", user.getNickName());
        return R.success().setData(map);
    }

    /**
     * 获取图形验证码
     * @param response res
     * @param uuId uuid
     * @throws IOException 错误
     */
    @GetMapping("/captcha.jpg/{uuId}")
    public void captcha(HttpServletResponse response, @PathVariable String uuId) throws IOException {
        loginService.captcha(response, uuId);
    }

    /**
     * 获取邮箱验证码
     * @param captcha 图形验证码
     * @return JSON
     */
    @PostMapping("/getCode")
    public R getEmailCode(@RequestBody @Validated Captcha captcha) {
        if (loginService.getEmailCode(captcha)) {
            return R.success().setMessage("验证码已发送至邮箱");
        }
        return R.error().setMessage("验证码发送失败");
    }

    /**
     * 验证邮箱验证码
     * @param email 邮箱
     * @param code 验证码
     * @return JSON
     */
    @PostMapping("/check")
    public R checkEmailCode(String email, Integer code) {
        if (loginService.checkEmailCode(email, code)) {
            return R.success().setMessage("邮箱验证成功！");
        }
        return R.error().setMessage("邮箱验证失败");
    }

    /**
     * 注册
     * @param registerVO 注册账号VO
     * @return JSON
     */
    @PostMapping("/register")
    public R register(@RequestBody @Validated RegisterVO registerVO) {
        if (loginService.register(registerVO)) {
            return R.success().setMessage("注册成功");
        }
        return R.error().setMessage("注册失败");
    }

    /**
     * 修改密码
     * @param findPWVO 密码修改账户VO
     * @return JSON
     */
    @PostMapping("/updatePassword")
    public R updatePassword(@RequestBody @Validated FindPWVO findPWVO) {
        if (loginService.updatePassword(findPWVO)) {
            return R.success().setMessage("修改密码成功");
        }
        return R.error().setMessage("修改密码失败");
    }
}
