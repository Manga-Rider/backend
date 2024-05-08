package com.mangarider.storage;

import java.io.InputStream;

public interface StorageService {
    void uploadFile(String key, InputStream inputStream);
    void deleteFile(String key);
    String getPublicUrl(String key);
}
