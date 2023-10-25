 package com.example.apigatewayservice.filter;

 import lombok.Data;
 import lombok.extern.slf4j.Slf4j;
 import org.springframework.cloud.gateway.filter.GatewayFilter;
 import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
 import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
 import org.springframework.core.Ordered;
 import org.springframework.http.server.reactive.ServerHttpRequest;
 import org.springframework.http.server.reactive.ServerHttpResponse;
 import org.springframework.stereotype.Component;
 import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    public LoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        
        GatewayFilter filter = new OrderedGatewayFilter((exchange, chain) -> {
            //filter에 정의한 내용시작

            //webflux에서는 HttpRequest/response 지원 하지않음, webexchange를 활용하면됨
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Logging Filter baseMessage: {}, {}", config.getBaseMessage(), request.getRemoteAddress());
            if (config.isPreLogger()) {
                log.info("Logging Filter Start: request id -> {}", request.getId());
            }
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                if (config.isPostLogger()) {
                    log.info("Logging Filter End: response code -> {}", response.getStatusCode());
                }
            }));
            //filter에 정의한 내용끝
        }
        , Ordered.LOWEST_PRECEDENCE//우선순위 낮음
//        , Ordered.HIGHEST_PRECEDENCE//우선순위 제일 높음
        );
        /**
         * OrderedGatewayFilter 의 filter 에 필터정의
         * public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
         *         return this.delegate.filter(exchange, chain);
         *     }
         *     
         *
         */
        
        return filter;
    }

    @Data
    public static class Config{
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
