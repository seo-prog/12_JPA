package com.ohgiraffers.section02;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class A_EntityManagerCRUDTests {

    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;

    @BeforeAll
    public static void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    @BeforeEach
    public void before() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterAll
    public static void close() {
        entityManagerFactory.close();
    }

    @AfterEach
    public void after() {
        entityManager.close();
    }

    @Test
    public void 메뉴코드로_메뉴조회_테스트() {

        int menuCode = 3;

        Menu foundMenu = entityManager.find(Menu.class, menuCode); // 기본적인 매니저 found 는 Id 만 넣을 수 있다.

        System.out.println("foundMenu = " + foundMenu);

        Assertions.assertNotNull(foundMenu);
    }

    @Test
    public void 새로운_메뉴_추가_테스트(){

        Menu menu = new Menu();
        menu.setMenuName("jpa 테스트 메뉴");
        menu.setMenuPrice(50000);
        menu.setCategoryCode(4);
        menu.setOrderableStatus("Y");

        // 데이터베이스의 상태 변화를 하나의 단위로 묶어주는 기능을 할 객체
        EntityTransaction entityTransaction = entityManager.getTransaction(); // 여기 안에서 작업을 하고 커밋이나 롤백 만나면 묶어줌

        entityTransaction.begin(); // 트랜잭션 활성화 // start 트랜잭션 // 이거 만난 이후로 DB 에 반영 !// 커밋이나 롤백을 만날때까지 !

        try{
            // 엔티티 매니저를 사용해 영속성 컨텍스트에 추가
            // persist -> insert
            entityManager.persist(menu); // 엔티티 매니저의 메모리 안에는 반영이 되었지만 db 에는 반영이 안됨. (메모리 단계의 저장) // 여기서 커밋을 해줘야 db 에 저장 !

            entityTransaction.commit(); // db 에 명령을 넣음

        }catch(Exception e){
            entityTransaction.rollback();
            e.printStackTrace();
        }
        // 데이터가 영속성 컨텍스트에 포함되어 있는지 확인
        Assertions.assertTrue(entityManager.contains(menu));

        // tests passed 가 통과했으면 체크 표시가 된다. 이게 통과가 되었다는 뜻. 통과가 아니면 체크가 안되었을듯.

    }

    @Test
    public void 메뉴_이름_수정_테스트(){
        Menu menu = entityManager.find(Menu.class, 2);
        System.out.println("menu = " + menu);

        // 데이터베이스의 상태 변화를 하나의 단위로 묶어주는 기능을 할 객체
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        // 1. 트랜잭션 -> begin
        // 2. 가지고 온 애로 수정을 원하는 거 set 해줌.
        // 3. 바꾼 이름과 지금 꺼내보고 두개가 같은지 (제대로 바꼈는지) 체크하면 완료 ! // tests passed 체크가 되면 성공 !

        try{
            // 반드시 트랜잭션 후 !!!!
            // entityManager 에서 원하는 데이터를 가지고 온 후에 이름만 set 으로 다시 넣어준 후 마무리 하면 이름이 바뀐다.
           menu.setMenuName("쿠우쿠우골드");
           entityTransaction.commit();
        }catch(Exception e){
            entityTransaction.rollback();
            e.printStackTrace();
        }

        Assertions.assertEquals("쿠우쿠우골드", entityManager.find(Menu.class, 2).getMenuName());

    }

    @Test
    public void 메뉴_삭제하기_테스트(){

        Menu menuToRemove = entityManager.find(Menu.class, 19009);

        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        try{
            entityManager.remove(menuToRemove);
            entityTransaction.commit();
        }catch(Exception e){
            entityTransaction.rollback();
            e.printStackTrace();
        }
        Menu removedMenu = entityManager.find(Menu.class, 19009);
        Assertions.assertNull(removedMenu);//  null 이면 성공 !
    }
}
