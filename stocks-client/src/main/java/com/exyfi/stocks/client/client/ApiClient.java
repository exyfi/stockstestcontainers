package com.exyfi.stocks.client.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.http.HttpHeaders;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApiClient {

    private final WebClient webCLient;

    public <T> Mono<T> invokePostAPI(String path, Class<T> clazz, Object body) throws WebClientException {
        log.debug("Calling market api, path={}", path);
        return webCLient.post().uri(String.join("", path))
                .bodyValue(body)
                .retrieve()
                .bodyToMono(clazz);
    }

    public <T> Flux<T> invokeGetAPI(String path, Class<T> clazz, HttpHeaders headers, MultiValueMap<String, Object> pathParams, MultiValueMap<String, Object> queryParams) throws WebClientException {
        log.debug("Calling market api, path={}", path);
        return webCLient.get().uri(String.join("", path))
                .retrieve()
                .bodyToFlux(clazz);
    }
}
