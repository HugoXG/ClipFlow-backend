package org.example.clipflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.model.FileInfo;
import jakarta.annotation.Resource;
import org.example.clipflow.config.QiNiuConfig;
import org.example.clipflow.entity.File;
import org.example.clipflow.mapper.FileMapper;
import org.example.clipflow.service.QiNiuFileService;
import org.springframework.stereotype.Service;

@Service
public class QiNiuFileServiceImpl implements QiNiuFileService {
    @Resource
    QiNiuConfig qiNiuConfig;

    @Resource
    FileMapper fileMapper;

    @Override
    public FileInfo getFileInfo(String fileKey) {
        BucketManager bucketManager = qiNiuConfig.buildBucketManager();
        try {
            return bucketManager.stat(qiNiuConfig.getBucketName(), fileKey);
        } catch (QiniuException e) {
            throw new RuntimeException("七牛云获取资源失败，服务器错误");
        }
    }

}
