package com.ohgiraffers.section03;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class A_EntityLifeCycleTests {

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

    /*
    * 영속성 컨텍스트는 엔티티 매니저가 엔티티 객체를 저장하는 공간으로,
    * 엔티티 객체를 보관하고 정리한다.
    * 엔티티 매니저가 생성될 떄 하나의 영속성 컨텍스트가 만들어진다..
    *
    * 엔티티의 생명주기
    * 비영속, 영속, 준영속
    *
    * */

    @Test
    void 비영속_테스트(){
        Menu foundMenu = entityManager.find(Menu.class, 5);

        // 객체만 생성하면, 영속성 컨텍스트나 db 와 관련없는 비영속 상태
        // (엔티티 기반이라 해도) 그냥 객체일 뿐. 비영속 상태 ! db 와 관련 x.
        Menu newMenu = new Menu();
        newMenu.setMenuCode(foundMenu.getMenuCode());
        newMenu.setMenuName(foundMenu.getMenuName());
        newMenu.setMenuPrice(foundMenu.getMenuPrice());
        newMenu.setCategoryCode(foundMenu.getCategoryCode());
        newMenu.setOrderableStatus(foundMenu.getOrderableStatus());

        Assertions.assertFalse(foundMenu == newMenu); // 값은 같지만 다른 객체. 관련이 없는 객체.

    }

    @Test
    void 영속성_연속_조회_테스트(){

        /*
        * 엔티티 매니저가 영속성 컨텍스트에 엔티티 객체를 저장(persist) 하면
        * 영속성 컨텍스트가 엔티티 객체를 관리하게 되고 이를 영속 상태라고 한다.
        * find(), jpql 을 사용한 조회도 영속 상태가 된다.*/

        Menu foundMenu1 = entityManager.find(Menu.class, 5); // 영속화(영속화 컨텍스트가 관리하는 객체)가 된다.
        Menu foundMenu2 = entityManager.find(Menu.class, 5);
        // find 를 만나면 select 뭐리문을 날리는데 이미 영속화가 되어있으면 이미 영속화 컨텍스트에 있는 값을 준다.
        // 전에 영속화가 된 코드를 또 조회하면 새롭게 조회하지 않고, 전에 영속화가 된 ( 이미 있는 ) 값을 주기에 주솟값이 같다.
        // 즉, 같은 값이다 ! 비교도 가능!

        Assertions.assertTrue(foundMenu1 == foundMenu2);
    }

    @Test
    void 영속성_객체_추가_테스트(){

        // 영속화의 느낌이 db 에 바로 넣지 않고 저장만 해두는 느낌(안전을 위해?) // 커밋을 만나야 db 에 넣어주는 의미.
        // 그러니 이미 가상의 메모리 상에는 등록(반영) 이 되어있으므로 조회를 하면 반영이 된 값이 조회된다.

        Menu menuToRegist = new Menu();
        menuToRegist.setMenuCode(1);
        menuToRegist.setMenuName("수박죽");
        menuToRegist.setMenuPrice(10000);
        menuToRegist.setCategoryCode(1);
        menuToRegist.setOrderableStatus("Y");

        entityManager.persist(menuToRegist); // 영속화 !
        Menu foundMenu = entityManager.find(Menu.class, 1); // 아직 db 에는 안넣었지만, 비교는 가능.
        // 이미 영속화가 되었기 떄문에 값을 코드로 꺼내올 수 있다.

        Assertions.assertTrue(foundMenu == menuToRegist);
        // 실제 db 에 반영이 안되었는데도 방금 삽입한 코드(영속화 이후라 조회 가능)와 입력한 코드를 비교한 결과는 true 가 나온다.

    }

    @Test
    void 준영속_detach_테스트(){

        Menu foundMenu = entityManager.find(Menu.class, 5);
        Menu foundMenu1 = entityManager.find(Menu.class, 13);

        /*
        * 영속성 컨텍스트가 관리하던 엔티티 객체가 더 이상 관리되지 않는 상태로 전환되면(detach), 해당 객체는 준영속 상태로 바뀐다.
        * 이는 jpa 객체의 변경 사항을 데이터베이스에 자동 반영되지 않는 상태
        *
        * Detach 메소드를 사용하면 특정 엔티티를 준영속 상태로 만들 수 있다.
        * 즉, 원하는 객체만 선택적으로 영속성 컨텍스트에서 분리할 수 있다.
        *
        * // Detach 로 이미 영속화 된 데이터를 잠시 영속화가 안되도록 살짝 빼둔다 라는 의미.
        * ex) 장바구니에 이것저것 담아둔 다음에 확정! 버튼을 누르면 db 에 만영되는 느낌.
        * */

        entityManager.detach(foundMenu1); // 준영속화 -- 객체의 값을 바꿔도 준영속화 상태이므로 반영이 안된다.

        foundMenu.setMenuPrice(5000);
        foundMenu1.setMenuPrice(5000);

        Assertions.assertEquals(5000,
                entityManager.find(Menu.class, 5).getMenuPrice());
        entityManager.merge(foundMenu1);
        // 이 코드가 없으면 밑에 값은 반영이 안된다..
        // 하지만 entityManager.merge(foundMenu1) 를 해주면 준영속화를 풀고 다시 영속화를 하겠다는 뜻으로, true 가 나온다.
        Assertions.assertEquals(5000,
                entityManager.find(Menu.class, 13).getMenuPrice());
    }

    @Test
    void close_테스트(){

        Menu foundMenu1 = entityManager.find(Menu.class, 12);
        Menu foundMenu2 = entityManager.find(Menu.class, 13);

        entityManager.close(); // entityManager 가 닫혔으므로 영속화를 못한다. // 닫힌 이후의 변경사항은 반영이 안된다.(당연함)
        // close 후에는 다이 entityManager 이 매니저를 못쓰고 새롭게 발급을 받아야 한다.

        foundMenu1.setMenuPrice(5000);
        foundMenu2.setMenuPrice(5000);

        Assertions.assertEquals(5000,
                entityManager.find(Menu.class, 12).getMenuPrice());

        Assertions.assertEquals(5000,
                entityManager.find(Menu.class, 13).getMenuPrice());
    }

    @Test
    void 삭제_remove_테스트(){

        /*
        *  remove : 엔티티를 영속성 컨텍스트 에서 삭제한다.
        * 트랜잭션을 커밋하는 순간 데이터베이스에 반영된다..*/
        // 이 매니저 안에 있는 영속성 컨텍스트 에서는 한번 한 즉, 12번 애는 다시는 안가지고 올꺼라는 의미.

        Menu foundMenu = entityManager.find(Menu.class, 12);

        entityManager.remove(foundMenu); // remove로 이미 지웠으면 다시 꺼내오려고 해도 가지고 오지 않는다.

        Menu refoundMenu = entityManager.find(Menu.class, 12);

        Assertions.assertEquals(12, foundMenu.getMenuCode());
        Assertions.assertEquals( null, refoundMenu);

    }

    @Test
    void 병합_merge_수정_테스트(){

        Menu menuToDetach = entityManager.find(Menu.class, 3);
        entityManager.detach(menuToDetach);

        menuToDetach.setMenuName("수박죽");

        Menu refoundMenu = entityManager.find(Menu.class, 3);

        System.out.println(menuToDetach.hashCode());
        System.out.println(refoundMenu.hashCode()); // 두 개의 주솟값이 다르다. 다른 객체라는거.
        // 하나 꺼냈다가 준영속 했다가 날리면 기존의 것을 살린게 아니라 쿼리문을 새로 날림

        entityManager.merge(menuToDetach);

        Menu mergedMenu = entityManager.find(Menu.class, 3);
        Assertions.assertEquals("수박죽", mergedMenu.getMenuName());
    }

//    @Test
//    void 병합_merge_수정_테스트2(){
//
//        Menu menuToDetach = entityManager.find(Menu.class, 3);
//        entityManager.detach(menuToDetach);
//
//        menuToDetach.setMenuName("수박죽");
//        menuToDetach.setMenuCode(999);
//
////        Menu refoundMenu = entityManager.find(Menu.class, 3);
////
////        System.out.println(menuToDetach.hashCode());
////        System.out.println(refoundMenu.hashCode()); // 두 개의 주솟값이 다르다. 다른 객체라는거.
//        // 하나 꺼냈다가 준영속 했다가 날리면 기존의 것을 살린게 아니라 쿼리문을 새로 날림
//
//        entityManager.merge(menuToDetach);
//
//        Menu mergedMenu = entityManager.find(Menu.class, 3);
//        Assertions.assertEquals("수박죽", mergedMenu.getMenuName());
//    }

    @Test
    void 병합_merge_삽입_테스트(){

        Menu menuToDetach = entityManager.find(Menu.class, 3);
        entityManager.detach(menuToDetach);// 준영속화

        menuToDetach.setMenuCode(999);
        menuToDetach.setMenuName("수박죽");

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(menuToDetach); // 머지 할 대상이 없으면 새롭게 날려버린다.
        // 새롭게 insert 를 날리게 된다.
        transaction.commit();

        Menu mergedMenu = entityManager.find(Menu.class, 999);
        Assertions.assertEquals("수박죽", mergedMenu.getMenuName());
    }
}



