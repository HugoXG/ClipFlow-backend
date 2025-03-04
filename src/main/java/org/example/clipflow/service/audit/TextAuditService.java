package org.example.clipflow.service.audit;

import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.util.StringMap;
import org.example.clipflow.config.QiNiuConfig;
import org.example.clipflow.constant.AuditMsgMap;
import org.example.clipflow.constant.AuditStatus;
import org.example.clipflow.entity.response.AuditResponse;
import org.example.clipflow.entity.user.json.DetailsJson;
import org.example.clipflow.entity.user.json.ResultChildJson;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * 文本审核
 *
 * @author hugo
 */
@Service
public class TextAuditService extends AbstractAuditService<String, AuditResponse>{
    // 构造依赖注入
    private final QiNiuConfig qiNiuConfig;
    public TextAuditService(QiNiuConfig qiNiuConfig) {
        this.qiNiuConfig = qiNiuConfig;
    }

    static String textUrl = "http://ai.qiniuapi.com/v3/text/censor"       ;
    static String textBody = "{\n" +
            "    \"data\": {\n" +
            "        \"text\": \"${text}\"\n" +
            "    },\n" +
            "    \"params\": {\n" +
            "        \"scenes\": [\n" +
            "            \"antispam\"\n" +
            "        ]\n" +
            "    }\n" +
            "}";

    public AuditResponse audit(String text) {
        AuditResponse auditResponse = new AuditResponse();
        auditResponse.setAuditStatus(AuditStatus.PROCESS);

        String body = textBody.replace("${text}", text);
        String method = "POST";
        // 获取token
        final String token = qiNiuConfig.getToken(textUrl, method, body, contentType);
        StringMap header = new StringMap();
        header.put("Host", "ai.qiniuapi.com");
        header.put("Authorization", token);
        header.put("Content-Type", contentType);
        Configuration cfg = new Configuration(Region.huadong());
        final Client client = new Client(cfg);
        try {
            Response response = client.post(textUrl, body.getBytes(), header, contentType);
            System.out.println(response.bodyString());
            final Map map = objectMapper.readValue(response.getInfo().split(" \n")[2], Map.class);
            if (!map.get("code").equals(200)) {
                throw new RuntimeException("昵称审核失败，服务器错误");
            }
            final ResultChildJson result = objectMapper.convertValue(map.get("result"), ResultChildJson.class);
            if (!result.getSuggestion().equals("pass")) {
                auditResponse.setAuditStatus(AuditStatus.FAILURE);
                final List<DetailsJson> details = result.getScenes().getAntispam().getDetails();
                for (DetailsJson detail : details) {
                    if (!detail.getLabel().equals("normal")) {
                        auditResponse.setMsg(AuditMsgMap.getInfo(detail.getLabel()));
                        break;
                    }
                }
            }
            auditResponse.setAuditStatus(AuditStatus.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return auditResponse;
    }
}
