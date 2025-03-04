package org.example.clipflow.service.audit;

public interface AuditService<T, R> {
    /**
     * 审核
     * @param task 任务
     * @return 审核结果
     */
    R audit(T task);
}
