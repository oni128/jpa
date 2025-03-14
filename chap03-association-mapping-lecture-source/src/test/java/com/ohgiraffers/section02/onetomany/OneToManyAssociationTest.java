package com.ohgiraffers.section02.onetomany;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;
import org.junit.jupiter.api.*;
public class OneToManyAssociationTest {
    private static EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;

    @BeforeAll
    public static void initFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    @BeforeEach
    public void initManager() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterAll
    public static void closeFactory() {
        entityManagerFactory.close();
    }

    @AfterEach
    public void closeManager() {
        entityManager.close();
    }

    @Test
    public void 일대다_연관관계_객체_그래프_탐색을_이용한_조회_테스트 () {
        int categoryCode = 10;

        CategoryAndMenu categoryAndMenu = entityManager.find(CategoryAndMenu.class, categoryCode);

        System.out.println("categoryAndMenu = " + categoryAndMenu);

        /* N+1 문제 발생 */
        List<Menu> menuList = categoryAndMenu.getMenuList();
        menuList.forEach(System.out::println);

    }
}
