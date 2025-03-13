package com.ohgiraffers.section01.problem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProblemOfUsingDirectSQL {

    private Connection con;

    @BeforeEach
    void setConnection() throws ClassNotFoundException, SQLException {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/menudb";
        String user = "swcamp";
        String password = "swcamp";

        Class.forName(driver);

        con = DriverManager.getConnection(url, user, password);
        con.setAutoCommit(false);

    }

    @AfterEach
    void closeConnection() throws SQLException {
        con.rollback();
        con.close();
    }

    /* 설명. JDBC API를 이용해 직접 SQL을 다룰 때 발생할 수 있는 문제점
     *   1. 데이터 변환, SQL 작성, JDBC API 코드 등의 중복 작성(개발 시간 증가, 유지보수성 저하)
     *   2. SQL에 의존하여 개발
     *   3. 패러다임 불일치(상속, 연관관계, 객체 그래프 탐색, 방향성)
     *   4. 동일성 보장 문제
     * */

    /* 목차. 1.  */
    @DisplayName("직접 SQL을 작성해야 메뉴를 조회할 때 발생하는 문제 확인")
    @Test
    void testDirectSelectSql() throws SQLException {

        // given
        String query = "SELECT MENU_CODE, MENU_NAME, MENU_PRICE, CATEGORY_CODE, "
                + "ORDERABLE_STATUS FROM TBL_MENU";

        // when
        Statement stmt = con.createStatement();
        ResultSet rset = stmt.executeQuery(query);

        List<Menu> menuList = new ArrayList<>();
        while (rset.next()) {
            Menu menu = new Menu();
            menu.setMenuCode(rset.getInt("MENU_CODE"));
            menu.setMenuName(rset.getString("MENU_NAME"));
            menu.setMenuPrice(rset.getInt("MENU_PRICE"));
            menu.setCategoryCode(rset.getInt("CATEGORY_CODE"));
            menu.setOrderableStatus(rset.getString("ORDERABLE_STATUS"));

            menuList.add(menu);
        }

        // then
        Assertions.assertTrue(!menuList.isEmpty());
        if (!menuList.isEmpty()) {
            menuList.forEach(System.out::println);
        }

        rset.close();
        stmt.close();
    }
    /* 목차. 2.  */
    @DisplayName("연관된 객체 문제 확인")
    @Test
    void testAssociationObject() throws SQLException {

        // given
        String query = "SELECT A.MENU_CODE, A.MENU_NAME, A.MENU_PRICE, B.CATEGORY_CODE, "
                + "B.CATEGORY_NAME, A.ORDERABLE_STATUS "
                + "FROM TBL_MENU A "
                + "JOIN TBL_CATEGORY B ON (A.CATEGORY_CODE = B.CATEGORY_CODE)";
        // when
        Statement stmt = con.createStatement();
        ResultSet rset = stmt.executeQuery(query);

        List<MenuAndCategory> menuAndCategories = new ArrayList<>();
        while (rset.next()) {
            MenuAndCategory menuAndCategory = new MenuAndCategory();
            menuAndCategory.setMenuCode(rset.getInt("MENU_CODE"));
            menuAndCategory.setMenuName(rset.getString("MENU_NAME"));
            menuAndCategory.setMenuPrice(rset.getInt("MENU_PRICE"));
            menuAndCategory.setCategory(new Category(rset.getInt("CATEGORY_CODE"),
                    rset.getString("CATEGORY_NAME")));
            menuAndCategory.setOrderableStatus(rset.getString("ORDERABLE_STATUS"));
        }

        // then

    }
}
