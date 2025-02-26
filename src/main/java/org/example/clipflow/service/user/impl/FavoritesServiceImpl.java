package org.example.clipflow.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.clipflow.entity.user.Favorites;
import org.example.clipflow.exception.BaseException;
import org.example.clipflow.mapper.FavoritesMapper;
import org.example.clipflow.service.user.FavoritesService;
import org.example.clipflow.service.user.FavoritesVideoService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class FavoritesServiceImpl extends ServiceImpl<FavoritesMapper, Favorites> implements FavoritesService {
    private final FavoritesVideoService favoritesVideoService;
    public FavoritesServiceImpl(FavoritesVideoService favoritesVideoService) {
        this.favoritesVideoService = favoritesVideoService;
    }

    @Override
    public void exists(Integer defaultFavoritesId) {
        LambdaQueryWrapper<Favorites> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Favorites::getId, defaultFavoritesId);
        if (this.count(lambdaQueryWrapper) == 0) {
            throw new BaseException("收藏夹选择错误");
        }
    }

    @Override
    public List<Favorites> listByUserId(Long userId) {
        // 根据用户id在favorites表中查询收藏夹列表favoritesList
        LambdaQueryWrapper<Favorites> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Favorites::getUserId, userId);
        List<Favorites> favoritesList = this.list(lambdaQueryWrapper)
                .stream()
                .map(favorites -> { // 过滤掉被删除的收藏夹
                    if (!favorites.isDeleted()) {
                        return favorites;
                    }
                    return null;
                })
                .toList();
        // 判空
        if (favoritesList.isEmpty()) {
            return Collections.emptyList();
        }
        // 获取每个收藏夹下的视频总数，用Map<Long, Long>存储，key为收藏夹id，value为视频总数
        Map<Long, Long> favoriteIdVideosCountMap =  favoritesVideoService.getFavoritesVideoCount(favoritesList);
        // 结果封装到Favorite对象中
        for (Favorites favorites : favoritesList) {
            Long favoriteId = favorites.getId();
            Long videosCount = favoriteIdVideosCountMap.get(favoriteId);
            favorites.setVideoCount(videosCount);
        }
        // 返回收藏夹列表
        return favoritesList;
    }
}
