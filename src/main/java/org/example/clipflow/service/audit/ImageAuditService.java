package org.example.clipflow.service.audit;

import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.util.StringMap;
import org.example.clipflow.constant.AuditStatus;
import org.example.clipflow.entity.response.AuditResponse;
import org.example.clipflow.entity.user.json.ResultChildJson;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class ImageAuditService extends AbstractAuditService<String, AuditResponse>{

    // 图片的内容审核 API 地址
    static String imageUlr = "http://ai.qiniuapi.com/v3/image/censor";

    // 根据官方文档的示例来自定义请求体
    static String imageBody = "{\n" +
            "    \"data\": {\n" +
            "        \"uri\": \"${url}\"\n" +
            "    },\n" +
            "    \"params\": {\n" +
            "        \"scenes\": [\n" +
            "            \"pulp\",\n" +
            "            \"terror\",\n" +
            "            \"politician\"\n" +
            "        ]\n" +
            "    }\n" +
            "}";
    @Override
    public AuditResponse audit(String fileKey) {
        AuditResponse auditResponse = new AuditResponse();
        auditResponse.setAuditStatus(AuditStatus.PROCESS);

        try {
            // 对文件名称进行编码
            String encodedFileName = URLEncoder.encode(fileKey, StandardCharsets.UTF_8).replace("+", "%20");
            String url = String.format("%s/%s", qiNiuConfig, encodedFileName);
            String body = imageBody.replace("${url}", ); // 把自定义请求体中的 $url 替换成 url 参数
            String method = "POST";
            // 获取token，这个token只能用于请求 imageUlr ，且请求方法是形参 method 真实的方法，请求体也是必须是形参 body 一样，还有内容类型 contentType
            final String token = qiNiuConfig.getToken(imageUlr, method, body, contentType);
            StringMap header = new StringMap();
            header.put("Host", "ai.qiniuapi.com");
            header.put("Authorization", token); // 设置 Authorization 为包含认证信息的 token
            header.put("Content-Type", contentType);
            Configuration cfg = new Configuration(Region.huadong());
            final Client client = new Client(cfg);
            Response response = client.post(imageUlr, body.getBytes(), header, contentType);

            // 获取响应的详细信息并按照 "\n" 进行分割，然后获取第三部分，因为图片审核结果中的 result 是第三部分
            // 然后通过 objectMapper 将字符串转换成 Map 对象
            final Map map = objectMapper.readValue(response.getInfo().split("\n")[2], Map.class);

            final ResultChildJson result = objectMapper.convertValue(map.get("result"), ResultChildJson.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
