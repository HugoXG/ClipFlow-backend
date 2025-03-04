package org.example.clipflow.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.clipflow.constant.AuditStatus;
import org.example.clipflow.constant.RedisConstant;
import org.example.clipflow.entity.response.AuditResponse;
import org.example.clipflow.entity.user.Favorites;
import org.example.clipflow.entity.user.User;
import org.example.clipflow.entity.vo.*;
import org.example.clipflow.exception.BaseException;
import org.example.clipflow.mapper.UserMapper;
import org.example.clipflow.service.audit.TextAuditService;
import org.example.clipflow.service.user.FavoritesService;
import org.example.clipflow.service.user.FollowService;
import org.example.clipflow.service.user.UserService;
import org.example.clipflow.utils.EncryptionUtil;
import org.example.clipflow.utils.RedisCaptchaUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final RedisCaptchaUtil redisCaptchaUtil;
    private final FavoritesService favoritesService;
    private final FollowService followService;
    private final TextAuditService textAuditService;

    public UserServiceImpl(RedisCaptchaUtil redisCaptchaUtil,
                           FavoritesService favoritesService,
                           FollowService followService,
                           TextAuditService textAuditService) {
        this.redisCaptchaUtil = redisCaptchaUtil;
        this.favoritesService = favoritesService;
        this.followService = followService;
        this.textAuditService = textAuditService;
    }

    @Override
    public boolean register(RegisterVO registerVO) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getEmail, registerVO.getEmail());
        if (this.getOne(lambdaQueryWrapper) != null) {
            throw new BaseException("该邮箱已注册");
        }

        Object o = redisCaptchaUtil.get(RedisConstant.EMAIL_CODE + registerVO.getEmail());
        if (ObjectUtils.isEmpty(o) || !o.equals(registerVO.getCode())) {
            throw new BaseException("验证码错误");
        }

        // 创建用户
        User user = new User();
        user.setNickName(registerVO.getNickName()); // 昵称
        user.setEmail(registerVO.getEmail()); // 邮箱
        user.setDescription("这个人很懒..."); // 描述
        user.setPassword(EncryptionUtil.encrypt(registerVO.getPassword())); // 密码
        this.save(user);

        Favorites favorites = new Favorites(); // 创建默认收藏夹
        favorites.setName("默认收藏夹");
        favorites.setUserId(user.getId());
        favoritesService.save(favorites);

        user.setDefaultFavoritesId(favorites.getId());
        this.updateById(user);
        return true;
    }

    @Override
    public boolean updatePassword(FindPWVO findPWVO) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getEmail, findPWVO.getEmail());
        if (this.getOne(lambdaQueryWrapper) == null) {
            throw new BaseException("用户不存在");
        }

        Object o = redisCaptchaUtil.get(RedisConstant.EMAIL_CODE + findPWVO.getEmail());
        if (ObjectUtils.isEmpty(o) || Integer.parseInt(o.toString()) != findPWVO.getCode()) {
            throw new BaseException("验证码错误");
        }

        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper
                .lambda()
                .set(User::getPassword, EncryptionUtil.encrypt(findPWVO.getNewPassword()))
                .eq(User::getEmail, findPWVO.getEmail());
        if (this.update(userUpdateWrapper)) {
            throw new BaseException("更新密码成功");
        } else {
            throw new BaseException("更新密码失败");
        }
    }

    @Override
    public UserVO getInfo(long userId) {
        final User userPO = this.getById(userId);
        if (ObjectUtils.isEmpty(userPO)) {
            return new UserVO();
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userPO, userVO);

        // 获取关注数、粉丝数
        long followsCount = followService.getFollowCount(userVO.getId());
        long fansCount = followService.getFansCount(userVO.getId());
        userVO.setFollow(followsCount);
        userVO.setFans(fansCount);

        return userVO;
    }

    @Override
    public boolean follows(long followsUserId, Long userId) {
        if (userId == null) {
            throw new BaseException("请先登录");
        }
        if (userId == followsUserId) {
            throw new BaseException("不能关注自己");
        }
        User user = this.getById(followsUserId);
        if (user == null) {
            throw new BaseException("关注用户不存在");
        }
        return followService.follows(userId, followsUserId);
    }

    @Override
    public void updateUser(UpdateUserVO updateUserVO, long userId) {
        final User oldUserPO = this.getById(userId);
        StringBuilder stringBuilder = new StringBuilder();
        // 对比新旧用户昵称和描述并进行拼接
        if (!oldUserPO.getNickName().equals(updateUserVO.getNickName())) {
            stringBuilder.append("昵称：").append(updateUserVO.getNickName()).append(";");
        }
        if (!oldUserPO.getDescription().equals(updateUserVO.getDescription())) {
            stringBuilder.append("描述：").append(updateUserVO.getDescription());
        }
        // 提交审核昵称和描述
        if (!stringBuilder.isEmpty()) {
            AuditResponse auditResponse = textAuditService.audit(stringBuilder.toString());
            if (!auditResponse.getAuditStatus().equals(AuditStatus.SUCCESS)) {
                throw new BaseException("审核失败，昵称或描述包含" + auditResponse.getMsg() + "内容");
            }
        }
        // todo 审核头像
        if (!oldUserPO.getAvatar().equals(updateUserVO.getAvatar())) {

        }
        BeanUtils.copyProperties(updateUserVO, oldUserPO);
        this.updateById(oldUserPO);
    }

    @Override
    public Page<User> getFollows(BasePage basePage, long userId) {
        // 获取关注列表followIds
        Collection<Long> followIds = followService.getFollows(basePage, userId);
        // 判空
        if (followIds.isEmpty()) {
            return new Page<>();
        }
        // 获取粉丝列表fanIds
        Collection<Long> fanIds = followService.getFans(basePage, userId);
        // 根据followIds获取User用户列表 todo 后期改为SafetyUser
        List<User> userList = this.getSafeUserListByIds(followIds);
        // 遍历User用户列表，查看每个User用户id是否在fanIds中，如果在，则表示互相关注，修改User中each属性为true
        for (User user : userList) {
            if (fanIds.contains(user.getId())) {
                user.setEach(true);
            }
        }
        // 将User用户添加到Page<User>的records中，设置total为关注列表的大小
        Page<User> userPage = new Page<>();
        userPage.setRecords(userList); // records是Page<User>的一个属性，用于存储分页数据
        userPage.setTotal(followIds.size()); // total是Page<User>的一个属性，用于存储总记录数
        return userPage;
    }

    @Override
    public Page<User> getFans(BasePage basePage, long userId) {
        // 获取粉丝列表fanIds
        Collection<Long> fanIds = followService.getFans(basePage, userId);
        // 判空
        if (fanIds.isEmpty()) {
            return new Page<>();
        }
        // 获取关注列表follows
        Collection<Long> followIds = followService.getFollows(basePage, userId);
        // 根据fanIds获取User用户列表 todo 后期改为SafetyUser
        List<User> userList = this.getSafeUserListByIds(fanIds);
        // 遍历User用户列表，查看每个User用户id是否在followIds中，如果在，则表示互相关注，修改User中each属性为true
        for (User user : userList) {
            if (followIds.contains(user.getId())) {
                user.setEach(true);
            }
        }
        // 将User用户添加到Page<User>的records中，设置total为关注列表的大小
        Page<User> userPage = new Page<>();
        userPage.setRecords(userList); // records是Page<User>的一个属性，用于存储分页数据
        userPage.setTotal(fanIds.size()); // total是Page<User>的一个属性，用于存储总记录数
        return userPage;
    }

    /**
     * 根据ids获取User用户列表，只获取id、nickName、avatar、description字段
     * @param ids 用户id列表
     * @return User用户列表
     */
    // todo 后期改为SafetyUser
    private List<User> getSafeUserListByIds(Collection<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        // 通过ids查询User用户列表
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .in(User::getId, ids)
                .select(User::getId, User::getNickName, User::isSex, User::getAvatar, User::getDescription);
        return this.list(lambdaQueryWrapper);
    }
}
