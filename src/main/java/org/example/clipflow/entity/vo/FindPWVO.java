package org.example.clipflow.entity.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FindPWVO {
    @NotBlank(message = "邮箱不能为空")
    private String email;

    @NotNull(message = "邮箱验证码不能为空")
    private Integer code;

    @NotBlank(message = "新密码不能为空")
    private String newPassword;
}
