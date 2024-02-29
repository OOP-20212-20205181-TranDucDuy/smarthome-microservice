package org.development.authservice.service;

import lombok.AllArgsConstructor;
import org.development.authservice.dto.*;
import org.development.authservice.enums.RoleType;
import org.development.authservice.exceptions.BadRequestException;
import org.development.authservice.exceptions.NotFoundException;
import org.development.authservice.exceptions.UnAuthorizedException;
import org.development.authservice.model.AccessTokenModel;
import org.development.authservice.model.RefreshTokenModel;
import org.development.authservice.repository.AccessTokenReposity;
import org.development.authservice.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {
    @Autowired
    private final AccessTokenReposity accessTokenReposity;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CallUserService callUserService;
    public AuthResponse register(RegisterRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        UserVO registeredUser = callUserService.registerUser(request);
        if(registeredUser.getEmail() == null){
            throw new BadRequestException("Some thing wrong!!!");
        }
        String accessToken = jwtUtil.generate(registeredUser.getEmail(), registeredUser.getRole(), "ACCESS");
        String refreshToken = jwtUtil.generate(registeredUser.getEmail(), registeredUser.getRole(), "REFRESH");
        return new AuthResponse(accessToken, refreshToken);
    }
    @Transactional
    public SignInResponse signin(SignInRequest request){
        UserVO registeredUser = callUserService.getUserByEmail(request.getEmail());

        if(!isVerifyPassword(request.getPassword(),registeredUser.getPassword())){
            throw new UnAuthorizedException();
        }
        String accessToken = jwtUtil.generate(registeredUser.getEmail(), registeredUser.getRole(), "ACCESS");
        String refreshToken = jwtUtil.generate(registeredUser.getEmail(), registeredUser.getRole(), "REFRESH");
        deleteToken(registeredUser.getId());
        AccessTokenModel accessTokenModel = AccessTokenModel.builder()
                .accessToken(accessToken)
                .build();
        accessTokenModel.setUserId(registeredUser.getId());
        accessTokenReposity.save(accessTokenModel);
        RefreshTokenModel refreshTokenModel = RefreshTokenModel.builder()
                .refreshToken(refreshToken)
                .build();
        refreshTokenModel.setUserId(registeredUser.getId());
        refreshTokenRepository.save(refreshTokenModel);
        return new SignInResponse(accessToken, refreshToken);
    }
    @Transactional
    public String refreshToken(String token){
        UserVO userVO = extractToken(token);
        String accessToken = jwtUtil.generate(userVO.getEmail(), userVO.getRole(), "ACCESS");
        String refreshToken = jwtUtil.generate(userVO.getEmail(), userVO.getRole(), "ACCESS");
        deleteToken(userVO.getId());
        AccessTokenModel accessTokenModel = AccessTokenModel.builder()
                .accessToken(accessToken)
                .build();
        accessTokenModel.setUserId(userVO.getId());
        accessTokenReposity.save(accessTokenModel);
        RefreshTokenModel refreshTokenModel = RefreshTokenModel.builder()
                .refreshToken(refreshToken)
                .build();
        refreshTokenModel.setUserId(userVO.getId());
        refreshTokenRepository.save(refreshTokenModel);
        return accessToken;
    }
    public void deleteToken(int userId){
        accessTokenReposity.updateAccessTokenModels(userId);
        refreshTokenRepository.updateRefreshTokenModels(userId);
    }
    public boolean isVerifyPassword(String decodePassword , String encodePassword){
        return passwordEncoder.matches(decodePassword, encodePassword);
    }
    public boolean verifyToken(String token) {
        UserInfo userInfo = jwtUtil.extractValuesFromToken(token);
        Optional<AccessTokenModel> accessTokenModel = accessTokenReposity.findAccessTokenModelByAccessToken(token);
        if(accessTokenModel.isEmpty()){
            return false;
        }
        return callUserService.verifyEmail(userInfo.getEmail());
    }
    public UserVO extractToken(String token) {
        UserInfo userInfo = jwtUtil.extractValuesFromToken(token);
        return callUserService.getUserByEmail(userInfo.getEmail());
    }

}