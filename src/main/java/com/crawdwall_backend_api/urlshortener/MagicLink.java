package com.crawdwall_backend_api.urlshortener;


import com.crawdwall_backend_api.utils.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "magik_link")
@AllArgsConstructor
@Builder
public class MagicLink extends BaseEntity {
    @Indexed
    private String shortCode;
    private String originalUrl;
    private String email;
    private String otpCode;
    private LocalDateTime expiresAt;
    private boolean used = false;
    private Long clickCount = 0L;

    // Helper method to check if link is expired
    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    // Helper method to check if link is valid
    public boolean isValid() {
        return !used && !isExpired();
    }

    // Method to increment click count
    public void incrementClickCount() {
        this.clickCount = (this.clickCount == null) ? 1L : this.clickCount + 1L;
    }
}
