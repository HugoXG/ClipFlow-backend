package org.example.clipflow.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.clipflow.entity.user.Favorites;
import org.example.clipflow.entity.user.FavoritesVideo;


import java.util.List;
import java.util.Map;

/**
* @author 18484
* @description 针对表【favorites_video】的数据库操作Service
* @createDate 2024-12-09 15:56:25
*/
public interface FavoritesVideoService extends IService<FavoritesVideo> {

    /**
     * 根据favoritesList查询每个收藏夹下的视频总数
     * @param favoritesList 收藏夹列表
     * @return Map<Long, Long> key为收藏夹id，value为视频总数
     */
    Map<Long, Long> getFavoritesVideoCount(List<Favorites> favoritesList);
}
