package org.example.clipflow.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.clipflow.entity.user.Favorites;
import org.example.clipflow.entity.user.FavoritesVideo;
import org.example.clipflow.mapper.FavoritesVideoMapper;
import org.example.clipflow.service.user.FavoritesVideoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author 18484
* @description 针对表【favorites_video】的数据库操作Service实现
* @createDate 2024-12-09 15:56:25
*/
@Service
public class FavoritesVideoServiceImpl extends ServiceImpl<FavoritesVideoMapper, FavoritesVideo>
implements FavoritesVideoService{

    @Override
    public Map<Long, Long> getFavoritesVideoCount(List<Favorites> favoritesList) {
        // 转为List<Long>存储收藏夹id
        List<Long> fIds = favoritesList
                .stream()
                .map(Favorites::getId)
                .toList();
        // 根据收藏夹id获取每个收藏夹下的视频总数
        LambdaQueryWrapper<FavoritesVideo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(FavoritesVideo::getFavoritesId, fIds);
        return this.list(lambdaQueryWrapper)
                .stream()
                .collect(Collectors.groupingBy(FavoritesVideo::getFavoritesId, Collectors.counting()));
    }
}
