package org.example.clipflow.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.clipflow.constant.RedisConstant;
import org.example.clipflow.entity.user.Follow;
import org.example.clipflow.entity.vo.BasePage;
import org.example.clipflow.exception.BaseException;
import org.example.clipflow.mapper.FollowMapper;
import org.example.clipflow.service.user.FollowService;
import org.example.clipflow.utils.RedisCacheUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow>implements FollowService {
    private final RedisTemplate redisTemplate;
    private final RedisCacheUtil redisCacheUtil;

    public FollowServiceImpl(RedisTemplate redisTemplate, RedisCacheUtil redisCacheUtil) {
        this.redisTemplate = redisTemplate;
        this.redisCacheUtil = redisCacheUtil;
    }

    @Override
    public boolean follows(long userId, long followsUserId) {
        Follow follow = new Follow();
        follow.setUserId(userId);
        follow.setFollowId(followsUserId);
        Date date = new Date();
        //方法1
//        try {
//            this.save(follow);
//            redisTemplate.opsForZSet().add(RedisConstant.USER_FOLLOW + userId, followsUserId, date.getTime());
//            redisTemplate.opsForZSet().add(RedisConstant.FOLLOW_USER + followsUserId, userId, date.getTime());
//            return true;
//        } catch (Exception e) {
//            LambdaQueryWrapper<Follow> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//            lambdaQueryWrapper.eq(Follow::getFollowId, followsUserId).eq(Follow::getUserId, userId);
//            this.remove(lambdaQueryWrapper);
//            //todo feed流
//            redisTemplate.opsForZSet().remove(RedisConstant.USER_FOLLOW + userId, followsUserId, date.getTime());
//            redisTemplate.opsForZSet().remove(RedisConstant.FOLLOW_USER + followsUserId, userId, date.getTime());
//            return false;
//        }
        //方法2
        LambdaQueryWrapper<Follow> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Follow::getFollowId, followsUserId).eq(Follow::getUserId, userId);
        // 使用redis有序集合ZSet存储数据
        if (this.getOne(lambdaQueryWrapper) == null) {
            this.save(follow);
            // 自己关注列表添加
            redisTemplate.opsForZSet().add(RedisConstant.USER_FOLLOW + userId, followsUserId, date.getTime());
            // 对方粉丝列表添加
            redisTemplate.opsForZSet().add(RedisConstant.USER_FAN + followsUserId, userId, date.getTime());
            return true;
        } else {
            this.remove(lambdaQueryWrapper);
            //todo feed流
            redisTemplate.opsForZSet().remove(RedisConstant.USER_FOLLOW + userId, followsUserId, date.getTime());
            redisTemplate.opsForZSet().remove(RedisConstant.USER_FAN + followsUserId, userId, date.getTime());
            return false;
        }
    }

    @Override
    public long getFollowCount(long userId) {
        LambdaQueryWrapper<Follow> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Follow::getUserId, userId);
        return this.count(lambdaQueryWrapper);
    }

    @Override
    public long getFansCount(long userId) {
        LambdaQueryWrapper<Follow> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Follow::getFollowId, userId);
        return this.count(lambdaQueryWrapper);
    }

    @Override
    public Collection<Long> getFollows(BasePage basePage, long userId) {
        // 判断是否需要分页查询
        if (ObjectUtils.isEmpty(basePage)) {
            // redis获取关注列表
            Object o = redisCacheUtil.zGet(RedisConstant.USER_FOLLOW + userId);
            // 判空
            if (ObjectUtils.isEmpty(o)) {
                // redis获取关注列表为空，从数据库获取
                LambdaQueryWrapper<Follow> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(Follow::getUserId, userId).orderByAsc(Follow::getGmtCreated);
                return this.list(lambdaQueryWrapper)
                        .stream()
                        .map(Follow::getFollowId)
                        .collect(Collectors.toList());
            }
            // Obj转成Collection，并使用流进行元素类型转换
            if (o instanceof Collection<?> collection) {
                return collection
                        .stream()
                        .map(e -> Long.valueOf(e.toString()))
                        .toList();
            }
        }
        // 需要分页查询
        Object o = redisCacheUtil.zGetByPage(RedisConstant.USER_FOLLOW + userId, basePage.getPage(), basePage.getLimit());
        // 判空
        if (ObjectUtils.isEmpty(o)) {
            // redis获取关注列表为空，从数据库获取
            LambdaQueryWrapper<Follow> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Follow::getUserId, userId).orderByAsc(Follow::getGmtCreated);
            // 分页查询
            return this.page(basePage.getIPage(new Follow()), queryWrapper)
                    .getRecords() // 获取分页数据
                    .stream()
                    .map(Follow::getFollowId)
                    .collect(Collectors.toList());
        }
        if (o instanceof Collection<?> collection) {
            return collection
                  .stream()
                  .map(e -> Long.valueOf(e.toString()))
                  .toList();
        }
        throw new BaseException("获取关注错误");
    }

    @Override
    public Collection<Long> getFans(BasePage basePage, long userId) {
        // 判断是否需要分页查询
        if (ObjectUtils.isEmpty(basePage)) {
            // redis获取关注列表
            Object o = redisCacheUtil.zGet(RedisConstant.USER_FAN + userId);
            // 判空
            if (ObjectUtils.isEmpty(o)) {
                // redis获取关注列表为空，从数据库获取
                LambdaQueryWrapper<Follow> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(Follow::getFollowId, userId).orderByAsc(Follow::getGmtCreated);
                return this.list(lambdaQueryWrapper)
                        .stream()
                        .map(Follow::getUserId)
                        .collect(Collectors.toList());
            }
            // Obj转成Collection，并使用流进行元素类型转换
            if (o instanceof Collection<?> collection) {
                return collection
                        .stream()
                        .map(e -> Long.valueOf(e.toString()))
                        .toList();
            }
        }
        // 需要分页查询
        Object o = redisCacheUtil.zGetByPage(RedisConstant.USER_FAN + userId, basePage.getPage(), basePage.getLimit());
        // 判空
        if (ObjectUtils.isEmpty(o)) {
            // redis获取关注列表为空，从数据库获取
            LambdaQueryWrapper<Follow> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Follow::getFollowId, userId).orderByAsc(Follow::getGmtCreated);
            // 分页查询
            return this.page(basePage.getIPage(new Follow()), queryWrapper)
                    .getRecords() // 获取分页数据
                    .stream()
                    .map(Follow::getUserId)
                    .collect(Collectors.toList());
        }
        if (o instanceof Collection<?> collection) {
            return collection
                    .stream()
                    .map(e -> Long.valueOf(e.toString()))
                    .toList();
        }
        throw new BaseException("获取粉丝错误");
    }

    public Collection<Long> getUserRelations(BasePage basePage, long userId, boolean isFollow) {
        // 判断是否需要分页查询
        if (ObjectUtils.isEmpty(basePage)) {
            // redis获取关注列表
            Object o = redisCacheUtil.zGet(isFollow ? RedisConstant.USER_FOLLOW : RedisConstant.USER_FAN + userId);
            // 判空
            if (ObjectUtils.isEmpty(o)) {
                // redis获取关注列表为空，从数据库获取
                LambdaQueryWrapper<Follow> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper
                        .eq(isFollow ? Follow::getUserId : Follow::getFollowId, userId)
                        .orderByAsc(Follow::getGmtCreated);
                return this.list(lambdaQueryWrapper)
                        .stream()
                        .map(isFollow ? Follow::getFollowId : Follow::getUserId)
                        .collect(Collectors.toList());
            }
            // Obj转成Collection，并使用流进行元素类型转换
            if (o instanceof Collection<?> collection) {
                return collection
                        .stream()
                        .map(e -> Long.valueOf(e.toString()))
                        .toList();
            }
        }
        // 需要分页查询
        Object o = redisCacheUtil.zGetByPage(isFollow ? RedisConstant.USER_FOLLOW : RedisConstant.USER_FAN + userId, basePage.getPage(), basePage.getLimit());
        // 判空
        if (ObjectUtils.isEmpty(o)) {
            // redis获取关注列表为空，从数据库获取
            LambdaQueryWrapper<Follow> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Follow::getUserId, userId).orderByAsc(Follow::getGmtCreated);
            // 分页查询
            return this.page(basePage.getIPage(new Follow()), queryWrapper)
                    .getRecords() // 获取分页数据
                    .stream()
                    .map(isFollow ? Follow::getUserId : Follow::getFollowId)
                    .collect(Collectors.toList());
        }
        if (o instanceof Collection<?> collection) {
            return collection
                    .stream()
                    .map(e -> Long.valueOf(e.toString()))
                    .toList();
        }
        throw new BaseException("获取关注错误");
    }
}
