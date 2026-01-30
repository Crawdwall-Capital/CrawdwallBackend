package com.crawdwall_backend_api.utils.fileupload.response;

public record FileUploadResponse(
        String fileUrl,
        String publicId
) {
}
