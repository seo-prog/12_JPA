package com.ohgiraffers.section05.access.subsection01;


import jakarta.persistence.*;

@Entity(name = "member_section05_subsection01")
@Table(name = "tbl_member_section05_subsection01")
@Access(AccessType.FIELD) // 필드 접근
public class Member {

    /*
    * 필드 접근은 기본 값이므로 해당 설정은 제거해도 동일하게 동작한다.
    * 또한 필드 레벨과 프로퍼티 레벨 모두 선언하면 프로퍼티 레벨을 우선으로 사용한다. ( 혼합방식도 가능 ) ( @Access -> 필드, 메소드에 모두 사용 가능)
    *
    * private 로 막아두고 게터세터로 접근 하도록 하는데 프로퍼티 방식으로 하면 이 캡슐화를 더욱 지킬 수 있다.
    *
    * */

    @Id
    @Column(name="member_no")
    private int memberNo;

    @Column(name = "member_id")
    private String memberId;

    @Column(name = "member_pwd")
    private String memberPwd;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "phone")
    private String phone;

    public Member() {
    }

    public Member(int memberNo, String memberId, String memberPwd, String nickname, String phone) {
        this.memberNo = memberNo;
        this.memberId = memberId;
        this.memberPwd = memberPwd;
        this.nickname = nickname;
        this.phone = phone;
    }

    public int getMemberNo() {
        System.out.println("getMemberNameNo 로 access 되는지 확인");
        return memberNo;
    }

    public void setMemberNo(int memberNo) {
        this.memberNo = memberNo;
    }

    public String getMemberId() {
        System.out.println("getMemberNameId 로 access 되는지 확인");
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberNo=" + memberNo +
                ", memberId='" + memberId + '\'' +
                ", memberPwd='" + memberPwd + '\'' +
                ", nickname='" + nickname + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
