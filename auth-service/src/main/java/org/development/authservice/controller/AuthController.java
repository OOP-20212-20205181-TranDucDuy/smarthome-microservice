package org.development.authservice.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.development.authservice.dto.*;
import org.development.authservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    @Autowired
    private AuthService authService;
    @CircuitBreaker(name = "user-service",fallbackMethod = "fallback")
    @TimeLimiter(name = "user-service")
    @PostMapping(value = "/register")
    public CompletableFuture<ResponseEntity<AuthResponse>> register(@RequestBody RegisterRequest request) {
        return CompletableFuture.supplyAsync(()->ResponseEntity.ok(authService.register(request)));
    }
    @PostMapping(value = "/signin")
    @CircuitBreaker(name = "user-service",fallbackMethod = "fallback")
    @TimeLimiter(name = "user-service")
    public CompletableFuture<ResponseEntity<SignInResponse>> signin(@RequestBody SignInRequest request) {
        return CompletableFuture.supplyAsync(()->ResponseEntity.ok(authService.signin(request)));
    }
    @GetMapping(value = "/refresh")
    public ResponseEntity<String> refresh(@RequestBody RefreshTokenRequest token){
        return ResponseEntity.ok(authService.refreshToken(token.getToken()));
    }
    public CompletableFuture<String> fallback(Throwable throwable){
        return CompletableFuture.supplyAsync(()->"[Fallback] User service encountered an error. Please try again later. Error : "+ throwable);
    }
    @PostMapping(value = "/verify")
    @CircuitBreaker(name = "user-service",fallbackMethod = "verifyFallback")
    @TimeLimiter(name = "user-service")
    public CompletableFuture<ResponseEntity<Boolean>> verifyToken(@RequestBody VerifyTokenRequest verifyTokenRequest
    ){
        return CompletableFuture.supplyAsync(()->ResponseEntity.ok(authService.verifyToken(verifyTokenRequest.getToken())));
    }
    @PostMapping(value = "/extractToken")
    @CircuitBreaker(name = "user-service",fallbackMethod = "fallback")
    @TimeLimiter(name = "user-service")
    public CompletableFuture<ResponseEntity<UserVO>> extractToken(@RequestBody VerifyTokenRequest verifyTokenRequest
    ){
        return CompletableFuture.supplyAsync(()->ResponseEntity.ok(authService.extractToken(verifyTokenRequest.getToken())));
    }
    public CompletableFuture<ResponseEntity<Boolean>> verifyFallback(Throwable throwable){
        return CompletableFuture.supplyAsync(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false));
    }
}