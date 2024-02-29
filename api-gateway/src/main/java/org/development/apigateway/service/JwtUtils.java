package org.development.apigateway.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.Key;
import java.util.Date;

@Service
public class JwtUtils {
    @Value("${application.spring.jwt.secret-key}")
    private String secret;
    private Key key;
    @Autowired
    private final RestTemplate restTemplate;

    public JwtUtils(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @PostConstruct
    public void initKey() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }
    public Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
    public boolean verifyToken(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String tokenRequest = "\""+token +"\"";
        // Create a request entity with the email data
        HttpEntity<String> requestEntity = new HttpEntity<>(tokenRequest, headers);
        System.out.println(requestEntity);
        // Make a POST request to the verifyEmail endpoint
        ResponseEntity<Boolean> response = restTemplate.postForEntity("lb://auth-service/api/auth/verify", requestEntity, Boolean.class);

        // Check for successful response and return the boolean value
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            // Handle non-OK response (e.g., log error, throw exception)
            System.err.println("Error verifying email: " + response.getStatusCodeValue());
            return false;
        }
    }
    public String getRole(String token){
        try {
            Claims claims = getClaims(token);
            String role = claims.get("role", String.class);
            System.out.println(role);
            return role;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public boolean isExpired(String token) {
        try {
            return getClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}