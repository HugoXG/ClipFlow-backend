package org.example.clipflow.service;

import com.qiniu.storage.model.FileInfo;
import org.example.clipflow.entity.File;

public interface QiNiuFileService extends FileCloudService{
    /**
     * 根据文件key获取文件信息
     * @param fileKey 文件key
     * @return 文件信息
     */
    FileInfo getFileInfo(String fileKey);
}
