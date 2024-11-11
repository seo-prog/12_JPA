package com.ohgiraffers.chap06securityjwt.user.service;

import com.ohgiraffers.chap06securityjwt.user.entity.User;
import com.ohgiraffers.chap06securityjwt.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;


    public User signup(User user) {

        user.setUserPass(encoder.encode(user.getUserPass())); // 입력받은 비밀번호 암호화 후 다시 넣어준거임.
        user.setState("Y"); // 활동중인 유저라는 뜻

        User Signup = userRepository.save(user);

        return Signup;
    }
}
