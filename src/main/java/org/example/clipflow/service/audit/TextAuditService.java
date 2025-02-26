package org.example.clipflow.service.audit;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.ciModel.auditing.TextAuditingRequest;
import com.qcloud.cos.model.ciModel.auditing.TextAuditingResponse;
import org.example.clipflow.config.TencentConfig;
import org.example.clipflow.entity.response.AuditResponse;
import org.example.clipflow.utils.ResponseUtil;
import org.example.clipflow.utils.TencentUtil;
import org.springframework.stereotype.Service;

import java.util.Base64;

/**
 * 文本审核
 *
 * @author hugo
 */
@Service
public class TextAuditService {
    // 构造依赖注入
    private final TencentUtil tencentUtil;
    private final TencentConfig tencentConfig;
    public TextAuditService(TencentUtil tencentUtil,
                            TencentConfig tencentConfig) {
        this.tencentConfig = tencentConfig;
        this.tencentUtil = tencentUtil;
    }

    public AuditResponse audit(String text) {
        AuditResponse auditResponse = new AuditResponse();
        String textBase64 = Base64.getEncoder().encodeToString(text.getBytes());
        try {
            COSClient client = tencentUtil.getClient();
            // 1.创建任务请求对象
            TextAuditingRequest request = new TextAuditingRequest();
            // 2.添加请求参数 参数详情请见 API 接口文档
            request.setBucketName(tencentConfig.getBucketName());
            // 2.1.1设置请求内容,文本内容的Base64编码
            request.getInput().setContent(textBase64);
            // 2.2设置审核模板（可选）
            request.getConf().setBizType("551c31fdb2b711ef965f525400662d48");
            // 3.调用接口,获取任务响应对象
            TextAuditingResponse response = client.createAuditingTextJobs(request);
            // 4.将任务响应对象转换为自定义的响应对象
            ResponseUtil.copyToAudit(response, auditResponse);
            client.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return auditResponse;
    }
}
