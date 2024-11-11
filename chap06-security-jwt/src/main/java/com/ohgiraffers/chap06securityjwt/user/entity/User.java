package com.ohgiraffers.chap06securityjwt.user.entity;


import com.ohgiraffers.chap06securityjwt.user.model.OhgiraffersRole;
import jakarta.persistence.*;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "tbl_user")
public class User {

    @Id
    @Column(name = "USER_NO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userNo;

    @Column(name = "USER_ID")
    private Integer userId;

    @Column(name = "USER_PASS")
    private String userPass;

    @Column(name = "USER_EMAIL")
    private String userEmail;

    @Column(name = "USER_ROLE")
    @Enumerated(value = EnumType.STRING)
    private OhgiraffersRole role;

    @Column(name = "USER_STATE")
    private String state;

    public User() {
    }

    public User(Integer userNo, Integer userId, String userPass, String userEmail, OhgiraffersRole role, String state) {
        this.userNo = userNo;
        this.userId = userId;
        this.userPass = userPass;
        this.userEmail = userEmail;
        this.role = role;
        this.state = state;
    }

    public List<String> getRoleList(){

        if(this.role.getRole().length()>0){
            return Arrays.asList(this.role.getRole().split(","));
            // 문자열을 리스트로 변환하는 작업 ! (1개든 2개든 !)
        }
        return new ArrayList<>();
    }

    public Integer getUserNo() {
        return userNo;
    }

    public void setUserNo(Integer userNo) {
        this.userNo = userNo;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public OhgiraffersRole getRole() {
        return role;
    }

    public void setRole(OhgiraffersRole role) {
        this.role = role;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "User{" +
                "userNo=" + userNo +
                ", userId=" + userId +
                ", userPass='" + userPass + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", role=" + role +
                ", state='" + state + '\'' +
                '}';
    }
}
