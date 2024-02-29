package org.development.userservice.service;

import lombok.extern.slf4j.Slf4j;
import org.development.userservice.dto.UserVO;
import org.development.userservice.enums.RoleType;
import org.development.userservice.exceptions.BadRequestException;
import org.development.userservice.exceptions.NotFoundException;
import org.development.userservice.model.UserModel;
import org.development.userservice.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.lang.Integer.parseInt;

@Service
@Slf4j
public class UserService implements IUserService{

    private final IUserRepository userRepository;
    @Autowired
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public UserVO save(UserVO userVO){
        if(userRepository.existsUserModelsByEmail(userVO.getEmail())){
            throw new BadRequestException("Account exists");
        }
        System.out.println(parseInt(String.valueOf(userRepository.count()))+1);
        UserModel user = UserModel.builder()
                .email(userVO.getEmail())
                .userId(parseInt(String.valueOf(userRepository.count()))+1)
                .password(userVO.getPassword())
                .role(RoleType.ROLE_USER)
                .build();
        userRepository.save(user);
        UserVO userResponseVO = UserVO.builder()
                .id(user.getUserId())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
        return userResponseVO;
    }
    public UserVO findUserByEmail(String email){
        UserModel user = userRepository.findUserModelByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        UserVO userVO = UserVO.builder()
                .id(user.getUserId())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
        return userVO;
    }
    public UserVO findUserById(int id){
        UserModel user = userRepository.findUserModelByUserId(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        UserVO userVO = UserVO.builder()
            .id(user.getUserId())
            .email(user.getEmail())
            .password(user.getPassword())
            .role(user.getRole())
            .build();
        return userVO;
    }
    public Boolean verifyEmail(String email){
        System.out.println(email);
        UserVO userVO = this.findUserByEmail(email);
        if(userVO == null){
            return false;
        }
        return true;
    }
}
