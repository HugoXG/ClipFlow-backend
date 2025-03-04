package org.example.clipflow.entity.response;

import lombok.Data;

@Data
public class AuditResponse {

    private Integer auditStatus;
    // true:违规 false:正常
    private Boolean flag;
    // 信息
    private String msg;

    private Long offset;

    public AuditResponse(Integer auditStatus,String msg){
        this.auditStatus = auditStatus;
        this.msg = msg;
    }
    public AuditResponse() {}

    public AuditResponse addMsg(String msg){
        this.msg += ( "," + msg );
        return this;
    }
}
