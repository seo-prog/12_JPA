package com.ohgiraffers.section03.projection;

import jakarta.persistence.*;
import org.junit.jupiter.api.*;

import java.util.List;

public class ProjectionTests {

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
    * 프로젝션 (projection)
    * SELECT 절에 조회할 대상을 지정하는 것을 프로젝션이라 한다.
    *
    * SELECT 프로젝션 대상 FROM
    *
    * 프로젝션은 4가지 방식이 있다.
    * 1. 엔티티 프로젝션
    * - 원하는 객체를 바로 조회할 수 있다.
    * - 조회된 엔티티는 영속성 컨텍스트가 관리한다.
    *
    * 2. 임베디드 타입 프로젝션
    * - 엔티티와 거의 비슷하게 사용되며 조회의 시작점이 될 수 없다.
    * - 임베디드 타입은 영속성 컨텍스트에서 관리되지 않는다.
    *
    * 3. 스칼라 타입 프로젝션
    * - 숫자, 문자, 날자 같은 기본 데이터 타입이다.( 주로 DB 기본 타입 )
    * - 스칼라 타입은 영속성 컨텍스트에서 관리하지 않는다.
    * ( 자바의 프리미티브 타입(기본자료형)과 비슷한 개념 )
    *
    * 4. NEW 명령어를 활용한 프로젝션
    * - 다양한 종류의 단순 값들을 DTO 로 바로 조회하는 방식으로 NEW 패키지명.dto명
    *   을 쓰면 해당 dto 로 바로 반환 받을 수 있다.
    * - new 명령어를 사용한 클래스의 객페는 엔티티가 아니므로 영속성 컨텍스트에서 관리되지 않는다.
    * */

    // 1. 엔티티 프로젝션(m 인 엔티티 객체 자체를 조회하는것 // 그 안에서 따로 꺼내는건 스칼라임)
    @Test
    void 단일_엔티티_프로젝션_테스트(){
        // 얘는(일반 엔티티 타입으로 조회한 경우) 영속성 컨텍스트가 관리하고 있으므로 변경하고 커밋하면 변경됨

        String jpql = "SELECT m FROM menu_section03 m";
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class).getResultList();
        Assertions.assertNotNull(menuList);

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        menuList.get(1).setMenuName("메뉴이름 변경 test");
        transaction.commit();
    }


    @Test
    void 양방향_연관관계_엔티티_프로젝션_테스트(){

        int menuCodeParameter = 3;

        String jpql = "SELECT m.category FROM bidirection_menu m WHERE m.menuCode = :menuCode";

        BiDirectionCategory category = entityManager.createQuery(jpql, BiDirectionCategory.class)
                .setParameter("menuCode", menuCodeParameter)
                .getSingleResult();

        Assertions.assertNotNull(category);
        System.out.println(category);

        List<BiDirectionMenu> list = category.getMenuList();
        for (BiDirectionMenu menu : list) {
            System.out.println(menu);
        }
    }

    @Test
    void 임베디드_프로젝션_테스트(){
        String jpql = "SELECT m.menuInfo FROM embedded_menu m";
        List<MenuInfo> menuList = entityManager.createQuery(jpql, MenuInfo.class).getResultList();
        Assertions.assertNotNull(menuList);
        for(MenuInfo menuInfo : menuList) {
            System.out.println(menuInfo);
        }
    }

    /*
    * @Embedded 은 데이터베이스에서 중복을 방지하는 역할은 하지 않으며 중복을 방지하려면 테이블에서 유니크 제약 조건을 추가해야 한다.
    *
    * @Embeddable 은 복합키를 표현할 때 사용하며, 해당 필드나 조합이 유일해야 하는 요구사항은 데이터베이스의 제약조건을 통해 해결해야 한다.
    *
    * */

    // 3. 스칼라 타입 프로젝션
    @Test
    void TypedQuery_이용한_스칼라_타입_프로젝션_테스트(){

        String jpql = "SELECT c.categoryName FROM category_section03 c";
        List<String> categoryList = entityManager.createQuery(jpql, String.class).getResultList();
        Assertions.assertNotNull(categoryList);
        for(String category : categoryList) {
            System.out.println(category);
        }
    }

    /*
    * 조회하려는 컬럼 값이 2개 이상인 경우, typeQuery 로 반환 타입을 지정하지 못한다..
    * 그 때 query 를 사용하여 Object[] 로 받을 수 있다.
    *
    * 카테고리 코드, 카테고리 네임 반환받아서 출력해보기*/

    @Test
    void 반환받아서_출력해보기(){

        String jpql = "SELECT c.categoryName, c.categoryCode FROM category_section03 c";

       List<Object[]> categoryList = entityManager.createQuery(jpql).getResultList();

       for(Object[] objects : categoryList) {
          for( Object  object: objects) {
              System.out.println(" " + object);
          }
//           List<Object> categoryList = entityManager.createQuery(jpql).getResultList();
//           for(Object

       }
       Assertions.assertNotNull(categoryList);
    }
}
