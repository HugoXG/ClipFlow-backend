package org.example.clipflow;

import jakarta.annotation.Resource;
import org.example.clipflow.config.TencentConfig;
import org.example.clipflow.utils.RedisCaptchaUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ClipFlowApplicationTests {
    @Resource
    TencentConfig tencentConfig;
    @Resource
    RedisCaptchaUtil redisCaptchaUtil;

    private static String passwordVO = "123456";

    @Test
    void contextLoads() {
    }

//    @Test
//    void doesBucketExist() {
//        COSClient client = tencentConfig.getClient();
//        boolean bucketExist = client.doesBucketExist(tencentConfig.getBucketName());
//        System.out.println(bucketExist);
//        client.shutdown();
//    }
//
//    @Test
//    void createAuditingTextJobs() {
//        // 原始内容
//        String originalContent = "操你妈";
//
//        // Base64 编码
//        String base64EncodedContent = Base64.getEncoder().encodeToString(originalContent.getBytes());
//
//        COSClient client = tencentConfig.getClient();
//        //1.创建任务请求对象
//        TextAuditingRequest request = new TextAuditingRequest();
//        //2.添加请求参数 参数详情请见 API 接口文档
//        request.setBucketName(tencentConfig.getBucketName());
//        //2.1.1设置请求内容,文本内容的Base64编码
//        request.getInput().setContent(base64EncodedContent);
//        //或是cos中的设置对象地址 不可同时设置
//        //request.getInput().setObject("1.txt");
//        //2.2设置审核模板（可选）
//        request.getConf().setBizType("551c31fdb2b711ef965f525400662d48");
//        //3.调用接口,获取任务响应对象
//        TextAuditingResponse response = client.createAuditingTextJobs(request);
//        System.out.println(response);
//        String s = response.getJobsDetail().getResult();
//        client.shutdown();
//    }
}
