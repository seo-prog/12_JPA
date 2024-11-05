package com.ohgiraffers.section03.primarykey;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.List;

public class PrimaryKeyMappingTests {

    private static EntityManagerFactory entityManagerFactory ;

    private EntityManager entityManager;

    @BeforeAll
    public static void initFactory(){
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    @BeforeEach
    public void init(){
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterEach
    public void destroy(){
        entityManager.close();
    }

    @AfterAll
    public static void destroyFactory(){
        entityManagerFactory.close();
    }
    /*
    * Primary Key 에는 @Id 어노테이션과 @GenerateValue 어노테이션을 사용한다.
    * @id 어노테이션은 엔티티 클래스에서 primary key 역할을 하는 필드를 지정할 떄 사용한다.
    * 데이터 베이스마다 기본 키를 생성하는 방식이 서로 다르다.
    * @GeneratesValue 는 다음과 같은 속성을 가지고 있다.
    *
    * -strategy : 자동 생성 전략을 지정
    * - GenerationType.INDENTITY : 기본 키 생성을 데이터베이스에 위임(MYSQL 의 autoIncrement)
    * - GenerationType.SEQUENCE : 데이터베이스 시퀀스 객체 사용(ORACLE 의 sequence) // 오라클은 autoIncrement 가 없으므로 이 방식을 사용하라는 뜻.
    * - GenerationType.TABLE : 키 생성 테이블 사용
    * - GenerationType.AUTO : 자동 선택
    * */

    @Test
    public void 식별자_매핑_테스트(){

        Member member = new Member();

        member.setMemberId("user01");
        member.setMemberPwd("pass01");
        member.setNickname("최서연");
        member.setPhone("010-1234-5678");

        Member member2 = new Member();

        member2.setMemberId("user02");
        member2.setMemberPwd("pass03");
        member2.setNickname("홍길동");
        member2.setPhone("010-1111-1111");

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(member); // persist는 한번에 하나씩 ! 따로 따로 넣어주던가 아님 반복문 이용해야댐
        entityManager.persist(member2);
        transaction.commit();

        String jpql = "SELECT A.memberNo FROM member_section03 A";
        // jpql 란? 자바에서 작성하는 쿼리문이고, db 에 영향을 받지 않는 자바쿼리문이다.
        // 엔티티 기준으로 쿼리가 작성되기 때문에 db 가 변경되어도 영향이 없다.
        // 우리는 memberNo 의 리스트를 받을 꺼기 때문에 리스트로 받은거다.( Integer 자료형인)
        List<Integer> memberNos = entityManager.createQuery(jpql, Integer.class).getResultList();
        System.out.println(memberNos);

    }
}
