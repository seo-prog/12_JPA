package com.ohgiraffers.section01.manytoone;

import jakarta.persistence.*;
import org.hibernate.sql.ast.tree.update.Assignable;
import org.junit.jupiter.api.*;

public class ManyToOneAssociationTests {

    /*
    *
    * Association Mapping 은 Entity 클래스 간의 관계를 매핑하는 것을 의미한다.
    * 이를 통해 객페를 이용해 데이터베이스의 테이블 간의 관ㄱㅖ를 매핑할 수 있다.
    *
    * 다중성에 의한 분류
    * 연관 관계가 있는 객체 관계에서는 실제로 연관을 가지는 객페의 수에 따라 분류된다.
    *
    * -N : 1 연관관계
    * -1 : N 연관관계
    * -1 : 1 연관관계
    *
    * JPA 에서 연관관계를 잘못 설정하면 성능과 데이터 일관성에 큰 영행을 줄 수 있다.
    * 각 연관관계 유형에 따하 jpa 가 데이터베이스와 상호작용 하는 방식이 달라지기 때문에
    * 잘못된 매핑 설정은 예상치 못한 쿼리를 발생 시키거나., 잘못된 접근을 유발헐 수 있다.
    *
    * 테이블의 연관 관계는 외래 키를 이용하여 양방량 연관 관계의 특징을 가진다.
    * 참조에 의한 객페의 연관 관계는 단방향이다.( 카테고리코드와 메뉴를 연관짓고 싶으면 둘 다 해줘야 양방향이다, 하나만 하면 단방향이 된다 !)
    * 객페 간의 연관관계를 양방량으로 만들고 싶은 경우 반대 쪽에서도 필드를 추가해 참조를 보관하면 된다.
    * 하지만 밀하게 이는 양방향 관게가 아니라 단방향 관계 2개로 볼 수 있다.
    * */
    
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
    
    /*
    * ManyToOne 은 다수의 엔티티가 하나의 엔티티를 참조하는 상황에서 사용된다.
    * 예를 들어 하나의 카테고리가 여러 개의 메뉴를 가질 수 있는 상황에서 메뉴 엔티티가 케테고리를 참조하는 것이다.
    * 이 때 메뉴 엔티티가 many, 카테고리 엔티티가 one 이 된다.
    * */
    
    @Test
    void 다대일_연관관계_조회_테스트(){
        int menuCode = 15;
        
        MenuAndCategory foundMenu = entityManager.find(MenuAndCategory.class, menuCode);
        Category menuCategory = foundMenu.getCategory();

        Assertions.assertNotNull(menuCategory);
        System.out.println("menuCategory = " + menuCategory);
    }
    
    @Test
    void 다대일_연관관계_객체지향쿼리_사용_조회_테스트(){
         String jpql = "SELECT c.categoryName FROM menu_and_category m JOIN m.category c WHERE m.menuCode = 15";
         // 이미 조인이 되어있음. 그러니 on 필요 없음 !
         String category = entityManager.createQuery(jpql, String.class).getSingleResult();
         
         Assertions.assertNotNull(category);
        System.out.println("category = " + category);
    }
    
    @Test
    void 다대일_연관관계_객체_삽입_테스트(){

         MenuAndCategory menuAndCategory = new MenuAndCategory();
         menuAndCategory.setMenuCode(99999);
         menuAndCategory.setMenuName("양미리구이");
         menuAndCategory.setMenuPrice(10000);

         Category category = new Category(); // 영속화가 안되있다. 그걸 참조하는 카테고리는 영속화가 안됨. 그러니까 에러가 남. 없는 카테고리를 등록하려해서.
         category.setCategoryCode(33333);
         category.setCategoryName("신규 카테고리");
         category.setRefCategoryCode(null);

         menuAndCategory.setCategory(category);
         menuAndCategory.setOrderableStatus("Y");

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(menuAndCategory);
        transaction.commit();

        MenuAndCategory foundMenu = entityManager.find(MenuAndCategory.class, 99999);
        Assertions.assertEquals(99999, foundMenu.getMenuCode());
        Assertions.assertEquals(33333, foundMenu.getCategory().getCategoryCode());

    }

    @Test
    void 영속성_삭제_테스트(){

        MenuAndCategory menuAndCategory = entityManager.find(MenuAndCategory.class, 99999);
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.remove(menuAndCategory);
        transaction.commit();

        MenuAndCategory deletedMenu = entityManager.find(MenuAndCategory.class, 99999);
        Category deletedCategory = entityManager.find(Category.class, 3333);
        System.out.println("deletedMenu = " + deletedMenu);
        System.out.println("deletedCategory = " + deletedCategory);
    }

    // 부모가 자식을 한명만 가지면 같이 지울 수 있는데, 여러 자식이 참조중이면 지울 수 없다.
    
    
    
    
}
