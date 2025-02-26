package org.example.clipflow.entity.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterVO {
    @NotBlank(message = "昵称不能为空")
    private String nickName;
    @NotBlank(message = "邮箱不能为空")
    @Email
    private String email;
    @NotBlank(message = "密码不能为空")
    private String password;
    @NotBlank(message = "邮箱验证码不能为空")
    private String code;
    @NotBlank(message = "邮箱不能为空")
    private String uuid;
}
