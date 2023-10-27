package com.example.apigatewayservice.filter;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 토큰관련
 */
@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private Environment env;

    public AuthorizationHeaderFilter(Environment env) {
        super(AuthorizationHeaderFilter.Config.class);
        this.env = env;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            //api호출할때 헤더에 로그인했을 때 받은 토큰을 전달할것 토큰이 잘들어이는지 인증처리되어있는지 발급이된건지등등의 결과를 리턴

            //authorization확인하려는 토큰 유무
            if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "no Authorization header", HttpStatus.UNAUTHORIZED);
            }
            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer", "");

            if(!isJwtValid(jwt)) {
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }
            return chain.filter(exchange);
            /*log.info("Global Filter baseMessage: {}, {}", config.getBaseMessage(), request.getRemoteAddress());
            if (config.isPreLogger()) {
                log.info("Global Filter Start: request id -> {}", request.getId());
            }
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                if (config.isPostLogger()) {
                    log.info("Global Filter End: response code -> {}", response.getStatusCode());
                }
            }));*/
        });
    }

    //jwt 정상 유무
    private boolean isJwtValid(String jwt) {
        boolean is = false;
        String subject = null;

        try {
            subject = Jwts.parser()
                    .setSigningKey(env.getProperty("token.secret"))//token생성시 사용한 키
                    .parseClaimsJws(jwt)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            is = false;
        }

        //변환한것 null 체크
        if(subject == null || subject.isEmpty()) { //todo stringutils...
            is = false;
        } else {
            is = true;
        }

        //id 동일여부

/**
 * token:
 *   expiration_time: 86400000 #1일
 *   secret: my_token
 */

        return is;
    }

    //spring webflux framwork에서 나온 타입,비동기
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        log.error(err);
        return response.setComplete();
    }

    public static class Config {

    }

}
