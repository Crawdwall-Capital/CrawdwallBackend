package com.crawdwall_backend_api.urlshortener;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MagicLinkRepository extends MongoRepository<MagicLink, String> {
    MagicLink findByShortCode(String shortCode);
}
