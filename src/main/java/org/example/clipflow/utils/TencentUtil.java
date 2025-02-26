package org.example.clipflow.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import org.example.clipflow.config.TencentConfig;
import org.springframework.stereotype.Component;

@Component
public class TencentUtil {

    private final TencentConfig tencentConfig;
    public TencentUtil(TencentConfig tencentConfig) {
        this.tencentConfig = tencentConfig;
    }

    public COSClient getClient() {
        COSCredentials cred = new BasicCOSCredentials(
                tencentConfig.getSecretId(),
                tencentConfig.getSecretKey()
        );
        Region cosRegion = new Region(tencentConfig.getRegion());
        ClientConfig clientConfig = new ClientConfig(cosRegion);
        return new COSClient(cred, clientConfig);
    }
}

