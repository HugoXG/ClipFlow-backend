package org.example.clipflow.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public class Captcha implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId
    @NotBlank(message = "uuid不能为空")
    private String uuid;

    @NotBlank(message = "code不能为空")
    private String code;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "请输入有效电子邮箱")
    @TableField(exist = false)
    private String email;

    private Date expireTime;
}
