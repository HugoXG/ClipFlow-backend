package org.example.clipflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import com.qiniu.storage.model.FileInfo;
import org.example.clipflow.entity.File;
import org.example.clipflow.mapper.FileMapper;
import org.example.clipflow.service.FileService;
import org.example.clipflow.service.QiNiuFileService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {
    @Resource
    QiNiuFileService qiNiuFileService;
    @Override
    public Long save(String fileKey, Long userId) {
        //获取文件信息
        FileInfo fileInfo = qiNiuFileService.getFileInfo(fileKey);
        if (fileInfo == null) {
            throw new IllegalArgumentException("参数不正确，无法找到文件");
        }
        // 考虑到用户可能会连续两次上传同一张图片，避免重复保存到数据库
        LambdaQueryWrapper<File> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(File::getFileKey, fileKey);
        File oldFile = this.getOne(queryWrapper);
        if (!ObjectUtils.isEmpty(oldFile)) {
            return oldFile.getId();
        }
        //保存文件信息
        final File file = new File();
        file.setFileKey(fileKey);
        file.setFormat(fileInfo.mimeType);
        file.setType(fileInfo.mimeType.equals("video") ? "视频" : "图片");
        file.setSize(fileInfo.fsize);
        file.setUserId(userId);
        this.save(file);

        return file.getId();
    }

    @Override
    public File getById(long id) {
        LambdaQueryWrapper<File> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(File::getId, id);
        return this.getOne(queryWrapper);
    }
}
