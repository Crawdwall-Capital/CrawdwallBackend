package com.crawdwall_backend_api.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class ExternalApiService {

    private final WebClient webClient;



    public ExternalApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }



}
    
    

    
    
    

