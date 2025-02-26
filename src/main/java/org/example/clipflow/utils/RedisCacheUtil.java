package org.example.clipflow.utils;

import org.example.clipflow.exception.BaseException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisCacheUtil {
    private final RedisTemplate<String, Object> redisTemplate;
    public RedisCacheUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 按 索引范围（index range） 逆序（从高到低）获取 ZSet 中的元素。
     * @param key 键
     */
    public Object zGet(String key) {
        try {
            return redisTemplate.opsForZSet().reverseRange(key, 0, -1);
        } catch (Exception e) {
            throw new BaseException("数据获取失败");
        }
    }

    /**
     * 分页获取ZSet中的元素
     * @param key 键
     * @param pageNum 页码
     * @param pageSize 每页大小
     */
    public Object zGetByPage(String key, long pageNum, long pageSize) {
        try {
            long start = (pageNum - 1) * pageSize;
            long end = pageNum * pageSize - 1;
            return redisTemplate.opsForZSet().reverseRange(key, start, end);
        } catch (Exception e) {
            throw new BaseException("数据获取失败");
        }
    }
}
