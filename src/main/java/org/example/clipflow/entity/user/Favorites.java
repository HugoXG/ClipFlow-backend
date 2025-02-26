package org.example.clipflow.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.clipflow.entity.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
public class Favorites extends BaseEntity {
    private long userId;
    private String name;
    private String description;
    // 收藏夹下的视频总数
    @TableField(exist = false)
    private Long videoCount;
}
