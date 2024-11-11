package com.ohgiraffers.chap06securityjwt.user.model;

public enum OhgiraffersRole {


    USER("USER"),

    ADMIN("ADMIN"),

    ALL("USER,ADMIN");

    private String role;

    OhgiraffersRole() {
    }

    OhgiraffersRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "OhgiraffersRole{" +
                "role='" + role + '\'' +
                '}';
    }
}
