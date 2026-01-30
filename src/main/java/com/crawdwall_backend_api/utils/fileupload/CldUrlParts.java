package com.crawdwall_backend_api.utils.fileupload;

import lombok.Builder;

@Builder
public record CldUrlParts(String cloud, String resource, String type,
                          Integer version, String publicId, String format) {
}
