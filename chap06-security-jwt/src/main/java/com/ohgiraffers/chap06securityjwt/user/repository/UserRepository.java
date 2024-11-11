package com.ohgiraffers.chap06securityjwt.user.repository;

import com.ohgiraffers.chap06securityjwt.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
