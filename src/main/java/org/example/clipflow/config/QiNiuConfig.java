package org.example.clipflow.config;

import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class QiNiuConfig {

    @Value("${QiNiu_ACCESS_KEY}")
    private String accessKey;

    @Value("${QiNiu_SECRET_KEY}")
    private String secretKey;

    @Value("${QiNiu_BUCKET_NAME}")
    private String bucketName;

    private Auth buildAuth() {
        return Auth.create(this.getAccessKey(), this.getSecretKey());
    }

    public String getToken(String url, String method, String body, String contentType) {

        final Auth auth = buildAuth();
        return "Qiniu " + auth.signQiniuAuthorization(url, method, body == null ? null : body.getBytes(), contentType);
    }



}
