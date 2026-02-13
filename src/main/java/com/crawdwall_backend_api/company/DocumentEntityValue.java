package com.crawdwall_backend_api.company;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentEntityValue {

    private DocumentEntityValueType documentEntityValueType;
    private String documentEntityValueUrl;
    private LocalDateTime submittedAt;
    
}
