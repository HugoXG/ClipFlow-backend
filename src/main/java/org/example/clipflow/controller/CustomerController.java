package org.example.clipflow.controller;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.example.clipflow.entity.vo.BasePage;
import org.example.clipflow.entity.vo.UpdateUserVO;
import org.example.clipflow.holder.UserHolder;
import org.example.clipflow.service.user.FavoritesService;
import org.example.clipflow.service.user.UserService;
import org.example.clipflow.utils.R;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/luckyjourney/customer")
public class CustomerController {
    //构造依赖注入
    private final UserService userService;
    private final FavoritesService favoritesService;
    public CustomerController(UserService userService,
                              FavoritesService favoritesService) {
        this.userService = userService;
        this.favoritesService = favoritesService;
    }


    /**
     * 关注/取关
     * @param followsUserId 被关注/取关者id
     * @return JSON
     */
    @PostMapping("/follows")
    public R follows(@NotNull @RequestParam long followsUserId) {
        log.info("接口：关注/取关");
        if (userService.follows(followsUserId, UserHolder.getUserId())) {
            return R.success().setMessage("已关注");
        }
        return R.success().setMessage("已取关");
    }


    // todo 后续移动到IndexController
    /**
     * 获取其他用户个人信息
     * @param userId 用户id
     * @return JSON
     */
    @GetMapping("/getInfo/{userId}")
    public R getInfo(@PathVariable long userId) {
        log.info("接口：获取其他用户个人信息");
        return R.success().setData(userService.getInfo(userId));
    }


    /**
     * 获取客户端用户个人信息
     *
     * @return JSON
     */
    @GetMapping("/getInfo/")
    public R getDefaultInfo() {
        log.info("接口：获取客户端用户个人信息");
        return R.success().setData(userService.getInfo(UserHolder.getUserId()));
    }


    /**
     * 修改用户信息
     * @param updateUserVO 用户信息
     * @return JSON
     */
    @PutMapping
    public R updateUser(@Validated @RequestBody UpdateUserVO updateUserVO) {
        log.info("接口：修改用户信息");
        long userId = UserHolder.getUserId();
        userService.updateUser(updateUserVO, userId);
        return R.success().setMessage("修改成功");
    }


    /**
     * 获取关注列表
     * @param basePage 分页参数
     * @param userId 用户id
     * @return JSON
     */
    @GetMapping("/follows")
    public R getFollows(BasePage basePage, long userId) {
        log.info("接口：获取关注列表");
        return R.success().setData(userService.getFollows(basePage, userId));
    }

    /**
     * 获取粉丝列表
     * @param basePage 分页参数
     * @param userId 用户id
     * @return JSON
     */
    @GetMapping("/fans")
    public R getFans(BasePage basePage, long userId) {
        log.info("接口：获取粉丝列表");
        return R.success().setData(userService.getFans(basePage, userId));
    }


    /**
     * 获取用户所有收藏夹
     * @return JSON
     */
    @GetMapping("/favorites")
    public R listFavorites(){
        final Long userId = UserHolder.getUserId();
        return R.success().setData(favoritesService.listByUserId(userId));
    }
}
