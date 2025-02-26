package org.example.clipflow.entity.user;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class Follow {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private long userId;
    private long followId;
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreated;
}
