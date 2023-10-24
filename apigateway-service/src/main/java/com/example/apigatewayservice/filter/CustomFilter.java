package com.example.apigatewayservice.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {

    public CustomFilter() {
        super(Config.class);
    }
    
    @Override
    public GatewayFilter apply(Config config) {
        //custom pre filter
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            
            log.info("Custom Pre filter: request id -> {}", request.getId());
            //custom post filter, post 필터적용
            return chain.filter(exchange).then(Mono.fromRunnable(() -> { //mono - webflux 스프링5추가 asyn서버지원시 단일값전달은 모노타입
              log.info("Custom Pro filter: request id -> {}", response.getStatusCode());
            }));
        });
    }

    public static class Config {
        //put configuration properties
    }
    
}
