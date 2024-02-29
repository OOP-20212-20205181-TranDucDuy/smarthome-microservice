package org.development.userservice.controller;

import lombok.AllArgsConstructor;
import org.development.userservice.dto.GetUserByEmailRequest;
import org.development.userservice.dto.UserVO;
import org.development.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/register")
    public ResponseEntity<UserVO> save(@RequestBody UserVO userVO) {
        return ResponseEntity.ok(userService.save(userVO));
    }
    @PostMapping(value = "/findOneByEmail")
    public ResponseEntity<UserVO> findUserByEmail(@RequestBody GetUserByEmailRequest request){
        return ResponseEntity.ok(userService.findUserByEmail(request.getEmail()));
    }
    @PostMapping(value = "/verifyEmail")
    public ResponseEntity<Boolean> verifyUser(@RequestBody GetUserByEmailRequest request){
        return ResponseEntity.ok(userService.verifyEmail(request.getEmail()));
    }
    @GetMapping(value = "/findOneById/{id}")
    public ResponseEntity<UserVO> findOneById(@PathVariable("id") int userId){
        return ResponseEntity.ok(userService.findUserById(userId));
    }
}
