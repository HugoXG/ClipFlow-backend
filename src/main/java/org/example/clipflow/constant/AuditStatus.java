package org.example.clipflow.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 审核状态
 * @Author: hugoXG
 * @CreateTime: 2023-10-29 01:16
 */
public class AuditStatus {
    public static Integer SUCCESS = 0; // 通过
    public static Integer PROCESS = 1; // 审核中
    public static Integer FAILURE = 2; // 失败
    public static Integer MANUAL = 3; // 需要人工审核

    private static Map<Integer,String> map = new HashMap<>();

    static {
        map.put(SUCCESS,"通过");
        map.put(PROCESS,"审核中");
        map.put(FAILURE,"失败");
        map.put(MANUAL,"需要人工审核");
    }
    public static String getName(Integer audit){
        return map.get(audit);
    }
}