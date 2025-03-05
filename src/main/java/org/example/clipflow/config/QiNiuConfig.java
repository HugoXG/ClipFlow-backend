package org.example.clipflow.config;

import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Region;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
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

    @Value("${QiNiu_CNAME}")
    public String CNAME;
    public Auth buildAuth() {
        return Auth.create(this.getAccessKey(), this.getSecretKey());
    }

    public String getToken(String url, String method, String body, String contentType) {

        final Auth auth = buildAuth();
        return "Qiniu " + auth.signQiniuAuthorization(url, method, body == null ? null : body.getBytes(), contentType);
    }


    public String upLoadToken(String type) {
        final Auth auth = buildAuth();
        return auth.uploadToken(bucketName, null, 300, new
                StringMap().put("mimeLimit", "video/*;image/*"));
    }

    public BucketManager buildBucketManager() {
        com.qiniu.storage.Configuration configuration = new com.qiniu.storage.Configuration(Region.huadong());
        Auth auth = this.buildAuth();
        return new BucketManager(auth, configuration);
    }
}
