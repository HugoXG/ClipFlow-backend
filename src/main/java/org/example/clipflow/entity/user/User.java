package org.example.clipflow.entity.user;

import com.baomidou.mybatisplus.annotation.TableField;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.clipflow.entity.BaseEntity;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
public class User extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String nickName;
    @Email
    private String email;
    @NotBlank(message = "密码不能为空")
    private String password;
    private String description;
    private boolean sex;
    private long defaultFavoritesId;
    private Long avatar;
    @TableField(exist = false)
    private Boolean each;
    @TableField(exist = false)
    private Set<String> roleName;
}
