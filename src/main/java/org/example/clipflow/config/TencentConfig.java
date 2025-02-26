package org.example.clipflow.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class TencentConfig {

    // 1 初始化用户身份信息（secretId, secretKey）。
    private final String secretId = "";
    private final String secretKey = "";
    private final String bucketName = "";
    private final String region = "";
}
