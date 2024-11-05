package com.ohgiraffers.section05.access.subsection02;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class PropertyAccessTests {

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
    void 프로퍼티_접근_테스트(){

        Member member = new Member();
        member.setMemberNo(1);
        member.setMemberId("user01");
        member.setMemberPwd("pass01");
        member.setNickname("홍길동");

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(member);
        transaction.commit();

        String jpql = "SELECT memberId FROM member_section05_subsection02 WHERE memberNo = 1";
        String registedId = entityManager.createQuery(jpql, String.class).getSingleResult();
        System.out.println(registedId);
        Assertions.assertEquals("user01", registedId);
    }


}
