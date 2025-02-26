package org.example.clipflow.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class SetDataFieldConfig implements MetaObjectHandler {
    //插入数据时自动填充
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("gmtCreated", new Date(), metaObject);
        this.setFieldValByName("gmtUpdated", new Date(), metaObject);
    }
    //更新数据时自动更新
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("gmtUpdated", new Date(), metaObject);
    }
}
