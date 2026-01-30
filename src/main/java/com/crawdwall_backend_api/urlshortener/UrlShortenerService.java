package com.crawdwall_backend_api.urlshortener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UrlShortenerService {
    private final MagicLinkRepository magicLinkRepository;
    public String generateShortCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

    public String createMagicLink(String url, String otp, String email) {

        String shortCode = generateShortCode();
        MagicLink link = MagicLink.builder()
                .shortCode(shortCode)
                .originalUrl(url)
                .email(email)
                .otpCode(otp)
                .build();

        return magicLinkRepository.save(link).getShortCode();
    }


    public String getOriginalUrl(String shortCode) {
        return magicLinkRepository.findByShortCode(shortCode).getOriginalUrl();
    }
}
