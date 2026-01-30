package com.crawdwall_backend_api.urlshortener;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/magik")
@RequiredArgsConstructor
public class MagikLinkController {
    private final UrlShortenerService urlShortenerService;

    @GetMapping("public/{shortCode}")
    public void redirectShortUrl(@PathVariable String shortCode, HttpServletResponse response) throws IOException {
        String originalUrl = urlShortenerService.getOriginalUrl(shortCode);
        response.sendRedirect(originalUrl);
    }

}
