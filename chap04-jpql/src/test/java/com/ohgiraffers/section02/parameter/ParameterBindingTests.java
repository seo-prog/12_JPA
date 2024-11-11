package com.ohgiraffers.section02.parameter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Scanner;

public class ParameterBindingTests {


    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;

    @BeforeAll
    public static void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    @BeforeEach
    public void initManager() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterAll
    public static void closeManager() {
        entityManagerFactory.close();
    }

    @AfterEach
    public void closeEntityManager() {
        entityManager.close();
    }

    /*

    * 파라미터를 바인딩 하는 방법
    * 1. 이름 기준 파라미터 ':' 다음에 이름 기준 파라미터를 지정한다.
    * 2. 위치 기준 파라미터 '?' 다음에 값을 주고 위치 값은 1부터 시작한다.
    *
    * */

    @Test
    void 이름_기준_파라미터_바인딩_메뉴_조회_테스트(){

        String menuNameParameter = "한우딸기국밥";

        String jpql = "SELECT m FROM menu_section02 m WHERE m.menuName = :menuName";
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class)
                .setParameter("menuName", menuNameParameter)
                .getResultList();

        Assertions.assertNotNull(menuList);
        for(Menu menu : menuList) {
            System.out.println(menu);
        }
    }

    @Test
    void 위치_기준_파라미터_바인딩_메뉴_목록_조회_테스트(){

        String menuNameParameter = "한우딸기국밥";
        String jpql = "SELECT m FROM menu_section02 m WHERE m.menuName = ?1";
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class)
                .setParameter(1, menuNameParameter)
                .getResultList();

        Assertions.assertNotNull(menuList);
        for(Menu menu : menuList) {
            System.out.println(menu);
        }
    }

    // 메뉴 이름 입력 시 입력한 값이 포함된 메뉴 조회

    @Test
    void 메뉴_이름_입력시_입력한_값이_포함된_메뉴_조회(){

//        Scanner sc = new Scanner(System.in);
//        System.out.println("조회하실 메뉴 이름을 입력해주세요 : ");
        String menuNameParameter = "한우";

        String jpql = "SELECT m FROM menu_section02 m WHERE m.menuName LIKE ?1";
        List <Menu> menu = entityManager.createQuery(jpql, Menu.class)
                .setParameter(1,"%"+menuNameParameter+"%" )
                .getResultList();
        Assertions.assertNotNull(menu);
        for(Menu menu1 : menu) {
            System.out.println(menu1);
        }
    }
}
