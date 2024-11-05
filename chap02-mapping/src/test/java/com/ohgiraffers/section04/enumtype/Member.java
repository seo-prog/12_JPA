package com.ohgiraffers.section04.enumtype;

import jakarta.persistence.*;

@Entity(name = "member_section04")
@Table(name = "tbl_member_section04")
public class Member {

    @Id
    @Column(name = "member_no")
    private int memberNo;

    @Column(name = "member_id")
    private String memberId;

    @Column(name = "member_pwd")
    private String memberPwd;

    @Column(name ="nickName")
    private String nickName;

    @Column(name = "member_role")
    // @Enumerated(EnumType.ORDINAL) // 숫자로 db 에 들어감.(ADMIN - 0, MEMBER -1)
     @Enumerated(EnumType.STRING) // 설정된 상수 필드의 이름으로 db 에 들어감(ADMIN, MEMBER 로 들어감)
    private RoleType memberRole;

    public Member() {
    }

    public Member(int memberNo, String memberId, String memberPwd, String nickName, RoleType memberRole) {
        this.memberNo = memberNo;
        this.memberId = memberId;
        this.memberPwd = memberPwd;
        this.nickName = nickName;
        this.memberRole = memberRole;
    }

    public int getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(int memberNo) {
        this.memberNo = memberNo;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberPwd() {
        return memberPwd;
    }

    public void setMemberPwd(String memberPwd) {
        this.memberPwd = memberPwd;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public RoleType getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(RoleType memberRole) {
        this.memberRole = memberRole;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberNo=" + memberNo +
                ", memberId='" + memberId + '\'' +
                ", memberPwd='" + memberPwd + '\'' +
                ", nickName='" + nickName + '\'' +
                ", memberRole=" + memberRole +
                '}';
    }
}
