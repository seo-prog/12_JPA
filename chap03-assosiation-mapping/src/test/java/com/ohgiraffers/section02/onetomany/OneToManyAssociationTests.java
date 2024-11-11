package com.ohgiraffers.section02.onetomany;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class OneToManyAssociationTests {

    private static EntityManagerFactory entityManagerFactory;

    private static EntityManager entityManager;

    @BeforeAll
    public static void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    @BeforeEach
    public void initEntityManager() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterAll
    public static void closeEntityManager() {
        entityManagerFactory.close();
    }

    @AfterEach
    public void closeEntityManagerFactory() {
        entityManager.close();
    }

    @Test
    void 일대다_연관관계_객체_조회_테스트(){

        int categoryCode = 10;

        CategoryAndMenu categoryAndMenu = entityManager.find(CategoryAndMenu.class, categoryCode);

        Assertions.assertNotNull(categoryAndMenu);
        System.out.println("categoryAndMenu = " + categoryAndMenu);
    }
    
    @Test
    void 일대다_연관관계_객체_삽입_테스트(){
        
        CategoryAndMenu categoryAndMenu = new CategoryAndMenu();
        categoryAndMenu.setCategoryName("일대다 카테고리 추가 테스트");
        categoryAndMenu.setRefCategoryCode(null);
        
        Menu menu = new Menu();
        menu.setMenuName("방어회");
        menu.setMenuPrice(50000);
        menu.setOrderableStatus("N");
        menu.setCategoryCode(categoryAndMenu); // 메뉴, 카테고리 둘 다 등록됨.
        // @ManyToOne 했기에 카테고리 등록을 했지만 메뉴도 같이 등록이 된것이다.
        
        categoryAndMenu.getMenuList().add(menu);
        
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(categoryAndMenu);
        transaction.commit();
        
        CategoryAndMenu foundCategoryAndMenu = entityManager.find(CategoryAndMenu.class, categoryAndMenu.getCategoryCode());
        System.out.println("foundCategoryAndMenu = " + foundCategoryAndMenu);
        
        
    }

}
