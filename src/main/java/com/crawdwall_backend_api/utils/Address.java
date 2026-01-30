package com.crawdwall_backend_api.utils;

import lombok.*;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {
    private String street;
    private String city;
    private String state;
    private String zip;
    private String country;
}
