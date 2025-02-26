package org.example.clipflow.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.clipflow.entity.user.Favorites;

import java.util.List;

public interface FavoritesService extends IService<Favorites> {
    void exists(Integer defaultFavoritesId);


    /**
     * 根据用户id获取收藏夹列表
     * @param userId 用户id
     * @return 收藏夹列表
     */
    List<Favorites> listByUserId(Long userId);
}
