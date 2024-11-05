package com.ohgiraffers.section03.primarykey;

import jakarta.persistence.*;


/*
* Entity 어노테이션은 jpa 에서 사용되는 클래스임을 표시한다.
* 이 어노테이션을 사용하면 해당 클래스가 데이터베이스의 테이블과 매핑된다.
*
* 프로젝트 내에 다른 패키지도 동일한 엔티티가 존대하는 경우 해당 엔티티를 식별하기 위한 중복되지 않는 name 을 지정 해 주어야 한다.
* enum, interface 에서는 사용할 수 없다.
* 저장할 필드에 final 을 사용하면 안된다.
* */

@Entity(name = "member_section03")
@Table(name = "tbl_member_section03")
// persistence 에서  <property name="hibernate.hbm2ddl.auto" value="create"/> 를 해줬기에
// 여기가 없는 테이블이면 새로 만드렁주고, 있는 테이블이면 기존꺼를 지우고 새로 만들어준다!
public class Member {

    @Id
    @Column(name="member_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
