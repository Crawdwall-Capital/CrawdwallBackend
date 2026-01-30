package com.crawdwall_backend_api.utils.fileupload;


import com.crawdwall_backend_api.utils.fileupload.response.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Map;

public interface FileUploadService {

    FileUploadResponse uploadFile(MultipartFile file, FileUploadCategory category, Map<String, String> extraData);
    void deleteFile(String fileUrl);
    void deleteFileAsync(String fileUrl);
    void deleteFileAsync(Collection<String> fileUrls);

}
