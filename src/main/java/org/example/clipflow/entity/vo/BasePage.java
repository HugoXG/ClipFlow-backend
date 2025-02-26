package org.example.clipflow.entity.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.springframework.util.ObjectUtils;

@Data
public class BasePage {
    private long page = 1L;
    private long limit = 15L;

    // 构造分页对象，用于查询
    public <T> IPage<T> getIPage(T t) {
        // 如果page和limit为空，则默认值为1和15
        if (ObjectUtils.isEmpty(page) || ObjectUtils.isEmpty(limit)) {
            page = 1L;
            limit = 15L;
        }
        return new Page<>(page, limit);
    }
}
