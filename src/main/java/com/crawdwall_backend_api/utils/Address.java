package com.crawdwall_backend_api.utils;

import lombok.Builder;

@Builder
public record Address(
        String city,
        String streetAddress,
        String postCode,
        String addressStartDate
){
    

}

