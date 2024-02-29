package org.development.apigateway.config;

import io.jsonwebtoken.Claims;
import org.development.apigateway.enums.RoleType;
import org.development.apigateway.service.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
@RefreshScope
@Component
public class AuthenticationFilter implements GatewayFilter {

    @Autowired
    private ApiValidator validator;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (validator.isSecured.test(request)) {
            if (authMissing(request)) {
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }
            String token;
            final String headerValue = request.getHeaders().getOrEmpty("Authorization").get(0);
            if (headerValue != null && headerValue.startsWith("Bearer ")) {
                token = headerValue.substring(7);
            } else {
                token = null;
            }
            if (jwtUtils.isExpired(token)) {
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }
            if(jwtUtils.verifyToken(token) == false){
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }
            if(validator.adminValidateApi.test(request)){
                if(!jwtUtils.getRole(token).equals(RoleType.ROLE_ADMIN.toString())){
                    return onError(exchange, HttpStatus.FORBIDDEN);
                }
            }
            if(validator.userValidateApi.test(request)){
                if(!jwtUtils.getRole(token).equals(RoleType.ROLE_USER.toString())) {
                    return onError(exchange, HttpStatus.FORBIDDEN);
                }
            }

        }
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private boolean authMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }
}