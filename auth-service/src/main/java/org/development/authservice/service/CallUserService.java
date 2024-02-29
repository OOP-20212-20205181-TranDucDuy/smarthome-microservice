package org.development.authservice.service;

import lombok.AllArgsConstructor;
import org.development.authservice.dto.RegisterRequest;
import org.development.authservice.dto.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class CallUserService {
    @Autowired
    private final RestTemplate restTemplate;
    public UserVO registerUser(RegisterRequest request) {
        return restTemplate.postForObject("lb://user-service/api/user/register", request, UserVO.class);
    }
    public UserVO getUserByEmail(String email) {
        String request = "\""+email+"\"";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(request, headers);
        return restTemplate.postForObject("lb://user-service/api/user/findOneByEmail",requestEntity,UserVO.class);
    }
    public boolean verifyEmail(String email){
        String request = "\""+email+"\"";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<Boolean> response = restTemplate.postForEntity("lb://user-service/api/user/verifyEmail", requestEntity, Boolean.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            System.err.println("Error verifying email: " + response.getStatusCodeValue());
            return false;
        }
    }

}
