package com.ohgiraffers.section05.groupfunction;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.List;

public class GroupFunctionTests {

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
        JPQL 의 그룹함수는 COUNT, MAX, MIN, SUM, AVG 오 SQL 의 그룹함수와 별반 차이가 없다.
        단, 몇 가지 주의사항이 있다.
        1. 그룹함수의 반환 타입은 결과 값이 정수면 LONG, 실수면 Double 로 반환
        2. 값이 없는 상태에서 count  를 제외ㅏ한 구룹 함수는 null 이 되고 count 만 0 이 된다.
        따라서 반환 값을 담기 위해 선언하는 벼수 타입을 기본자료형으로 하게 되면, 조회 결과를 언박싱 할 때 NPE 가 발생한다.
        3. 그룹 함수의 반환 자료형은 Long or Double 형 이기 때문에 having 절에서 그룸 함수 결과 값을 비교하기 위한
        파라미터 타입은 Long or Double 로 해야한다.
     */

    @Test
    void 특정_카테고리에_등록된_메뉴수_조회(){

        int categoryCodeParameter = 4;
        String jpql = "SELECT COUNT(m.menuPrice) FROM menu_section05 m WHERE m.categoryCode = :categoryCode";
        long countOfMenu = entityManager.createQuery(jpql, Long.class)
                .setParameter("categoryCode", categoryCodeParameter)
                .getSingleResult();

        Assertions.assertTrue(countOfMenu >= 0);
        System.out.println(countOfMenu);
    }

    @Test
    void 카운트_제외_조회결과_없을_시(){

        int categoryCodeParameter = 1;

        String jpql = "SELECT SUM(m.menuPrice) FROM menu_section05 m WHERE m.categoryCode = :categoryCode";
        Long sumOfPrice = entityManager.createQuery(jpql, Long.class)
                .setParameter("categoryCode", categoryCodeParameter)
                .getSingleResult();


        System.out.println(sumOfPrice);

    }

    @Test
    void groupby절과_having절을_사용한테스트(){

        // having 절에서의 파라미터는 Long 을 사용한다.
        // 그룹함수의 반환 타입이 Long 이므로..

        long minPrice = 50000L;
        String jpql = "SELECT m.categoryCode, SUM(m.menuPrice) FROM menu_section05 m"+
                " GROUP BY m.categoryCode "+
                " HAVING SUM(m.menuPrice) >= :minPrice";

        List<Object[]> result = entityManager.createQuery(jpql, Object[].class)
                .setParameter("minPrice", minPrice)
                .getResultList();

        Assertions.assertNotNull(result);
        for(Object[] row : result) {
            for(Object categoryCode : row) {
                System.out.println(categoryCode);
            }
        }

    }
}
