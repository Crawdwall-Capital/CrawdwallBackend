package com.crawdwall_backend_api.utils;

import lombok.Builder;

@Builder
public record RefinedPagination(
        int page,
        int size
) {

}
