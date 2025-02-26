package org.example.clipflow.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.clipflow.entity.user.Follow;
import org.example.clipflow.entity.vo.BasePage;

import java.util.Collection;

public interface FollowService extends IService<Follow> {
    /**
     * 关注/取关
     * @param userId 关注者id
     * @param followsUserId 被关注者id
     * @return 布尔
     */
    boolean follows(long userId, long followsUserId);

    /**
     * 获取关注人数
     * @param userId 用户id
     * @return 关注人数
     */
    long getFollowCount(long userId);

    /**
     * 获取粉丝人数
     * @param userId 用户id
     * @return 粉丝人数
     */
    long getFansCount(long userId);

    // todo 待优化，合并getFollows和getFans
    /**
     * 获取关注列表
     * @param basePage 分页参数
     * @param userId 用户id
     * @return 关注列表
     */
    Collection<Long> getFollows(BasePage basePage, long userId);

    /**
     * 获取粉丝列表
     * @param basePage 分页参数
     * @param userId 用户id
     * @return 粉丝列表
     */
    Collection<Long> getFans(BasePage basePage, long userId);
}
