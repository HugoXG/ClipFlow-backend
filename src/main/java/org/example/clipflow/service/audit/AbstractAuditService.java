package org.example.clipflow.service.audit;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.example.clipflow.config.QiNiuConfig;
import org.example.clipflow.entity.response.AuditResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;

/**
 * @description:  统一封装审核逻辑，并留给子类进行编排或者调用普通逻辑
 * @Author: Xhy
 * @CreateTime: 2023-11-03 12:05
 */
@Service
public abstract class AbstractAuditService<T,R> implements AuditService<T,R> {

    @Resource
    protected QiNiuConfig qiNiuConfig;


    protected ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    static final String contentType = "application/json";

    protected AuditResponse audit() {
        return null;
    }
}
