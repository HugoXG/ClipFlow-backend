package org.example.clipflow.service.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.clipflow.entity.user.User;
import org.example.clipflow.entity.vo.*;

public interface UserService extends IService<User> {
    /**
     * 注册
     * @param registerVO VO
     * @return 布尔
     */
    boolean register(RegisterVO registerVO);

    /**
     * 更新密码
     * @param findPWVO VO
     * @return 布尔
     */
    boolean updatePassword(FindPWVO findPWVO);

    /**
     * 获取用户信息
     * @param userId 用户id
     * @return UserVO
     */
    UserVO getInfo(long userId);

    /**
     * 关注/取关
     *
     * @param followsUserId 被关注/取关者id
     * @param userId
     * @return 布尔
     */
    boolean follows(long followsUserId, Long userId);

    /**
     * 修改用户信息
     *
     * @param updateUserVO VO
     * @param userId
     */
    void updateUser(UpdateUserVO updateUserVO, long userId);

    /**
     * 分页获取关注列表
     * @param basePage 分页参数
     * @param userId 用户id
     * @return 关注列表
     */
    Page<User> getFollows(BasePage basePage, long userId);

    /**
     * 分页获取粉丝列表
     * @param basePage 分页参数
     * @param userId 用户id
     * @return 粉丝列表
     */
    Page<User> getFans(BasePage basePage, long userId);
}
