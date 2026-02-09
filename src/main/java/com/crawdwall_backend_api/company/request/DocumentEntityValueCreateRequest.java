package com.crawdwall_backend_api.company.request;

import com.crawdwall_backend_api.company.DocumentEntityValueType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentEntityValueCreateRequest {
    @NotNull
    private DocumentEntityValueType documentEntityValueType;
    @NotNull
    private String documentEntityValueUrl;
    
}
