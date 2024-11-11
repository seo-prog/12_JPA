package com.ohgiraffers.section01.simple;

import jakarta.persistence.*;
import org.junit.jupiter.api.*;

import java.util.List;

public class SimpleJPQLTests {

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
    * jpql (java Persistence Query Language)
    * jpql 은 엔티티 객체를 중심으로 개발할 수 있는 객체 지향 쿼리아디.
    * sql 보다 간결하며 특정 dbms 에 의존하지 않는다.
    * 방언을 통해 해당 dbms 에 맞는 sql 을 실행하게 된다.
    * jpql 은 find() 메소드를 통한 조회와 다르게 항상 데이터베이스에 sql 을 실행해서 결과를 조회한다.
    * */
    /*
    *
    * jpql 의 기본 문법
    * SELECT, UPDATE, SELECT 등의 키워드는 SQL 과 동일하다.
    * INSERT 는 PERSIST 를 사용하몀 된다.
    * 키워드는 대소문자를 구분하지 않지만, 엔티티ㅣ와 속성은 대ㅐ소문자를 구문함에 유의한다. 별칭을 사용하는 것이 권장된다.
    * 별칭 없이도 쿼리가 작동할 수 있으나, 권장된다.
    *
    * jpql 사용 방법은 다음과 같다.
    * 1. 작성한 jpql 을 createQuery() 를 통해 쿼리 객체로 만든다.
    * 2. 쿼리 객페는 TypeQuery, Query 두 가지가 있다.
    * - TypeQuery : 반환할 타입을 명확하게 지정하는 방식일 때 사용하며 쿼리 객체의 메소드 실행 겨ㅑㄹ과로
    * 저정된 타입이 반환더ㅣㄴ다.
    * - Query  : 반환할 타입을 명확하게 지정할 수 없을 떄 사용허ㅏ며 쿼리 객페 메소드의 실행 결과로 Object 또는 Object[] 가 반환된다.
    *
    * 3. 쿼리 객체에서 제공하느 ㄴ메소드 getSingleResult () 또는 getResultList() 를 호출해 쿼리를 실행하고 db 를 조회한다.
    * - getSingleResult() : 결과가 정확히 한 행일 경우 사용하며 없거나 많으면 에러가 발생
    * - getResultList () : 결과가 2행 이상일 경우 사용하며 컬렉션을 반환, 결과가 없으면 빈 컬렉션을 반환,
    *
    * */

    @Test
    void 쿼리를_이용한_단일메뉴_조회_테스트(){
        String jpql = "SELECT m.menuName FROM menu_section01 AS m WHERE m.menuCode = 6";
        Query query = entityManager.createQuery(jpql);

        Object resultMenuName = query.getSingleResult();
        Assertions.assertTrue(resultMenuName instanceof String);
        Assertions.assertEquals("생마늘샐러드", resultMenuName);

    }

    @Test
    void Query를_이용한_다중행_조회(){
        String jpql = "SELECT m.menuName FROM menu_section01 AS m";
        Query query = entityManager.createQuery(jpql);

        List<Object> foundMenu = query.getResultList();
        Assertions.assertNotNull(foundMenu);
        for (Object menu : foundMenu) {
            System.out.println(menu);
        }
    }

    @Test
    void TypesQuery를_이용한__조회_테스트(){

        String jpql = "SELECT m FROM menu_section01 AS m WHERE m.menuCode = 6";
        // jpql 에 * 로 전체조회를 못한다. 전체조회를 원하면 테이블에 별칭을 지어주고 그 별칭으로 전체조회 가능
        TypedQuery<Menu> query = entityManager.createQuery(jpql, Menu.class);

        Menu foundMenu = query.getSingleResult();
        Assertions.assertEquals(6, foundMenu.getMenuCode());
        System.out.println(foundMenu);
    }

    @Test
    void TypesQuery를_이용한_다중행_조회_테스트(){

        String jpql = "SELECT m FROM menu_section01 AS m";
        TypedQuery<Menu> query = entityManager.createQuery(jpql, Menu.class);
        List<Menu> foundMenu = query.getResultList();
        Assertions.assertNotNull(foundMenu);
        for (Menu menu : foundMenu) {
            System.out.println(menu);
        }

    }

    @Test
    void distinct_활용한_중복제거_여러행_조회_테스트(){

        String hpql = "SELECT DISTINCT m.categoryCode FROM menu_section01 AS m";
        TypedQuery<Integer> query = entityManager.createQuery(hpql, Integer.class);
        List<Integer> categoryList = query.getResultList();

        Assertions.assertNotNull(categoryList);
        for(int category : categoryList) {
            System.out.println(category);
        }

    }

    // 카테고리 코드가 6번, 10번인 메뉴 조회
    // 메뉴 이름에 마늘이 포함된 메뉴

    @Test
    void 카테고리_코드가_6번_10번인_메뉴_조회(){
         String jpql = "SELECT m FROM menu_section01 AS m WHERE m.categoryCode = 6 OR m.categoryCode = 10";
        TypedQuery<Object> query = entityManager.createQuery(jpql, Object.class);
        List<Object> categoryList = query.getResultList();
        Assertions.assertNotNull(categoryList);
        for(Object category : categoryList) {
            System.out.println(category);
        }

    }

    @Test
    void 메뉴_이름에_마늘이_포함된_메뉴(){

        String jpql = "SELECT m FROM menu_section01 AS m WHERE m.menuName LIKE '%마늘%'";
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class).getResultList();

        Assertions.assertNotNull(menuList);
        System.out.println(menuList);
    }

    /*
    * MyBatis 는 복잡한 sql 쿼리와 데이터베이스 성능 최적화 측면ㅇ에서, JPA 는 객체 지향적인 데이터 접근과 자동화된 상태 관리 측면에서 주로 사용.
    *
    * JPA 는 객체 지향적인 패러다임을 통해 DB 와의 연동을 자동화하지만, 복잡한 SQL 뭐리나 대량의 데이터 처리에서는 성능에 제한이 있다.
    * 이런 경우 MYbATIS 는 명시적이고 복잡한 쿼리를 작성하는 데 적합하며, 최적화에 유리.
    *
    * Mybatis 는 복잡한 조인. 서브퉈리, 다양한 함수 등을 철처리할 쌔 더 유연하고, 쿼리의 복잡성을 완전히 개발자가 컨트롤할 수 있다.
    * sql 을 직접 작성하여 사용하므로, 객페 변환 없이 순수 sql 을 실행할 수 있다.
    * jpa 는 orm 을 통해 객페로 변환하는 과정을 거쳐해하고, 복잡해질수록 성능적 문제와 추가적인 시간이 소요,
    *
    *
    * */

}
