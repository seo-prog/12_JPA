package com.ohgiraffers.chap06securityjwt.user.controller;


import com.ohgiraffers.chap06securityjwt.user.entity.User;
import com.ohgiraffers.chap06securityjwt.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody User user) {

        User signup = userService.signup(user);

        if(Objects.isNull(signup)){
            return ResponseEntity.status(500).body("가입 실패 !");
        }
        return ResponseEntity.ok(signup);
    }
}
