package org.example.clipflow.utils;

import com.qcloud.cos.model.ciModel.auditing.TextAuditingResponse;
import org.example.clipflow.entity.response.AuditResponse;

public class ResponseUtil {
    public static void copyToAudit(TextAuditingResponse response, AuditResponse auditResponse) {
        if (response == null || response.getJobsDetail() == null) {
            throw new IllegalArgumentException("审核结果出错");
        }
        auditResponse.setJobId(response.getJobsDetail().getJobId());
        auditResponse.setState(response.getJobsDetail().getState());
        auditResponse.setResult(Integer.valueOf(response.getJobsDetail().getResult()));
        auditResponse.setMsg(response.getJobsDetail().getLabel());
    }
}
