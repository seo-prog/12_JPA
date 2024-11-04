package com.ohgiraffers.section01.problem;
// test 안에 작성하면 메소드, 클래스 단위로 실행시킬 수 있다. // Junit 기반.
// 즉, main 과 이런 객체지향적인 경로를 따로 안잡아줘도 된다는 뜻. 메소드 단위로 문법 이런거 즉시 체킹함. // 즉, 검증을 할 수 있다.

import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProblemIOfUsingDirectSQLTest {

    /*
    * Junit = 단위 테스트를 위한 프레임워크.
    * 코드의 품질을 개선하고 유지보수성을 놏이기 위해 사용. 메소드 단위로 테스트를 수행하며
    * 예상한 결과와 실제 결과를 비교하여 테스트를 수행.
    * 이를 통해 개발자들은 코드의 문제점을 발견하고 수정할 수 있다.
    * */

    private Connection con;

    @BeforeEach // 테스트메소드가 동작하기 전 실행
    void setConnection() throws ClassNotFoundException, SQLException {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/menudb";
        String user = "gangnam";
        String password = "gangnam";

        Class.forName(driver);
        con = DriverManager.getConnection(url,user,password);
        con.setAutoCommit(false);

    }

    @AfterEach // 테스트메소드가 실행된 후 실행
    void closeConnection() throws SQLException {
        con.rollback();
        con.commit();
    }

    /*

    * JDBC API 를 이용해 직접 SQL 을 다룰 때 발생할 수 있는 문제점
    * 1. 데이터 변환, SQL 작성, JDBC API 코드 등의 중복 작성 (개발 시간 증가, 유지보수성 저하)
    * 2. SQL 에 의존하여 개발
    * 3. 패러다임 불일치
    * 4. 동일성 보장 문제
    *
    * */

    // 1. 데이터 변환, SQL 작성, JDBC API 코드 등의 중복 작성 (개발 시간 증가, 유지보수성 저하)
    @DisplayName("직접 SQL 을 작성하여 메뉴를 조회할 떄 발생하는 문제 확인")
    @Test
    void testDirectSelectSql() throws SQLException {
        String query = "SELECT MENU_CODE, MENU_NAME, MENU_PRICE, CATEGORY_CODE,"+
                "ORDERABLE_STATUS FROM TBL_MENU";

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        List<Menu> menuList = new ArrayList<>();

        while (rs.next()){
            Menu menu = new Menu();
            menu.setMenuCode(rs.getInt("MENU_CODE"));
            menu.setMenuName(rs.getString("MENU_NAME"));
            menu.setMenuPrice(rs.getInt("MENU_PRICE"));
            menu.setCategory(rs.getInt("CATEGORY_CODE"));
            menu.setOrderableStatus(rs.getString("ORDERABLE_STATUS"));
            menuList.add(menu);
        }

        Assertions.assertNotNull(menuList);
        for(Menu menu : menuList){
            System.out.println(menu);
        }
        rs.close();
        stmt.close();
    }

    // 2. SQL 에 의존하여 개발
    /*
    * 요구사항 변경에 따라 어플리케이션 수정이 sql 수정으로도 이어진다.
    * 이러한 수정에 영향을 미치는 것은 오류를 발생시킬 가능성도 있지만 유지보수성에도 악영향을 미친다.
    * 또한 객체를 사용할 때 sql 에 의존하면 객펭레 값이 무엇이 들어있는지 확인하기 위해 sql 을 매번 살펴야 한다..*/

    @DisplayName("조회 항목 변경에 따른 의존성 확인")
    @Test
    void testChangeSelectColums() throws SQLException {

        String query = "SELECT MENU_CODE, MENU_NAME FROM TBL_MENU";

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        List<Menu> menuList = new ArrayList<>();

        while (rs.next()) {
            Menu menu = new Menu();

            menu.setMenuCode(rs.getInt("MENU_CODE"));
            menu.setMenuName(rs.getString("MENU_NAME"));
            menuList.add(menu);
        }
        Assertions.assertNotNull(menuList);
        for(Menu menu : menuList){
            System.out.println(menu);
        }
        rs.close();
        stmt.close();
    }

    // 연관된 객체 문제
    /*
    * 연관된 객체가 있는 경우, 객체 간의 관계를 수동으로 매핑해야 하기 떄문에 코드가 복잡해지고 유지보수가 어려워지는 문제가 있다.*/

    @DisplayName("연관된 객체 문제 확인")
    @Test
    void testAssosiatedObject() throws SQLException {

        String query = "SELECT A.MENU_CODE, A.MENU_NAME, A.MENU_PRICE, B.CATEGORY_CODE,"+
                "B.CATEGORY_NAME, A.ORDERABLE_STATUS FROM TBL_MENU A JOIN TBL_CATEGORY B ON (A.CATEGORY_CODE = B.CATEGORY_CODE)";

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        List<MenuAndCategory> menuAndCategories = new ArrayList<>();

        while (rs.next()){
            MenuAndCategory menuAndCategory = new MenuAndCategory();
            menuAndCategory.setMenuCode(rs.getInt("MENU_CODE"));
            menuAndCategory.setMenuName(rs.getString("MENU_NAME"));
            menuAndCategory.setMenuPrice(rs.getInt("MENU_PRICE"));
            menuAndCategory.setCategory(new Category(rs.getInt("CATEGORY_CODE"),
                            rs.getString("CATEGORY_NAME")));
            menuAndCategory.setOrderableStatus(rs.getString("ORDERABLE_STATUS"));
            menuAndCategories.add(menuAndCategory);
        }

        Assertions.assertNotNull(menuAndCategories);
        for(MenuAndCategory menuand : menuAndCategories){
            System.out.println(menuand);
        }
        rs.close();
        stmt.close();

    }

    // 4. 동일성 보장 문제
    @DisplayName("조회한 두 개의 행을 담은 객체의 동일성 비교 테스트")
    @Test
    void testEquals() throws SQLException {
        String query = "SELECT MENU_CODE, MENU_NAME FROM TBL_MENU WHERE MENU_CODE = 12";

        Statement stmt1 = con.createStatement();
        ResultSet rs1 = stmt1.executeQuery(query);

       Menu menu1 = null;
       while (rs1.next()) {
           menu1 = new Menu();
           menu1.setMenuCode(rs1.getInt("MENU_CODE"));
           menu1.setMenuName(rs1.getString("MENU_NAME"));
       }
       Statement stmt2 = con.createStatement();
       ResultSet rs2 = stmt2.executeQuery(query);

       Menu menu2 = null;
       while (rs2.next()) {
           menu2 = new Menu();
           menu2.setMenuCode(rs2.getInt("MENU_CODE"));
           menu2.setMenuName(rs2.getString("MENU_NAME"));

       }
       rs2.close();
       stmt2.close();
       rs1.close();
       stmt1.close();
       // 주솟값이 다르니 다른 객체로 된다. 동등객체를 동일객체로 만들려면 여기서는 따로 작업을 더 해줘야한다. jpa 는 알아서 해준다 !

       Assertions.assertFalse(menu1 == menu2);
    }
}
