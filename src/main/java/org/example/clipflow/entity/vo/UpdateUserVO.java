package org.example.clipflow.entity.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 修改用户信息VO
 *
 * @Author: hugo
 */
@Data
public class UpdateUserVO {
    @NotBlank(message = "昵称不能为空")
    private String nickName;

    private long avatar;

    private boolean sex;

    private String description;

}
