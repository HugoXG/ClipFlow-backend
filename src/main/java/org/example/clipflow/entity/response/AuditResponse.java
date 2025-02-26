package org.example.clipflow.entity.response;

import lombok.Data;

@Data
public class AuditResponse {
    private String jobId;
    private String state;
    private Integer result;
    private String msg;

    public AuditResponse() {}
}
