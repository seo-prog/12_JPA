package com.ohgiraffers.section05.access.subsection01;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class FieldAccessTests {

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

    @Test
    void 필드_접근_테스트(){

        // 직관적이고 편함
        // 필드 접근 방식은 캡슐화가 안되고 있는중. 직접 필드에 접근한다. 즉, getter, setter 를 활용을 안하고 있다는 의미. - 게터에 찍어둔 값이 콘솔에 안찍힌다.
        // 필드접근- 필드에서 직접 접근해 사용한다.

        Member member = new Member();

        member.setMemberNo(1);
        member.setMemberId("user01");
        member.setMemberPwd("pass01");

        entityManager.persist(member);

        Member foundMember = entityManager.find(Member.class, 1);
        Assertions.assertEquals(member, foundMember);
        System.out.println(foundMember);
    }
}
