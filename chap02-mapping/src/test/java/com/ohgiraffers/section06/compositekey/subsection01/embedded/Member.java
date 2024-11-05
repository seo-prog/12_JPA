package com.ohgiraffers.section06.compositekey.subsection01.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity(name = "member_section06_subsection01")
@Table(name = "tbl_number_section06_subsection01")
public class Member {

    @EmbeddedId
    private MemberPK memberPK; // 객체 자체를 넣기 힘드니까 memberPK 에ㅐ서 Serializable 를 상속받으면 직렬화하여 넣을 수 있도록 한다.

    @Column(name = "phone")
    private String phone;

    @Column(name="address")
    private String address;

    public Member() {
    }

    public Member(MemberPK memberPK, String phone, String address) {
        this.memberPK = memberPK;
        this.phone = phone;
        this.address = address;
    }

    public MemberPK getMemberPK() {
        return memberPK;
    }

    public void setMemberPK(MemberPK memberPK) {
        this.memberPK = memberPK;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberPK=" + memberPK +
                ", phone='" + phone + '\'' +
                ", adress='" + address + '\'' +
                '}';
    }
}
