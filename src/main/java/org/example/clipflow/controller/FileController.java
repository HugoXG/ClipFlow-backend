package org.example.clipflow.controller;

import jakarta.annotation.Resource;
import org.example.clipflow.config.QiNiuConfig;
import org.example.clipflow.holder.UserHolder;
import org.example.clipflow.service.FileService;
import org.example.clipflow.utils.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/luckyjourney/file")
public class FileController {
    @Resource
    QiNiuConfig qiNiuConfig;

    @Resource
    FileService fileService;

    /**
     * 保存文件
     * @param fileKey 文件key
     * @return 上传成功
     */
    @PostMapping
    public R save(String fileKey) {
        return R.success().setData(fileService.save(fileKey, UserHolder.getUserId()));
    }

    /**
     * 获取上传token
     * @param type 文件类型
     * @return 上传token
     */
    @GetMapping("/getToken")
    public R getToken(String type) {
        return R.success().setData(qiNiuConfig.upLoadToken(type));
    }
}
