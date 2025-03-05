package org.example.clipflow.service;

import org.example.clipflow.entity.File;

public interface FileService {
    Long save(String fileKey, Long userId);

    File getById(long id);
}
