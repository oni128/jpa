package com.ohgiraffers.section01;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EntityManagerLifeCycleTest {
    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @BeforeAll
    public static void initFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    // 예제마다 다른 EntityManager를 부여해야함
    @BeforeEach
    public void initManager() {

        /* 설명. EntityManager가 생성될 때마다 고유의 새로운 영속성 컨텍스트(Entity를 관리하는 자바의 창고)가 생성된다. */
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterEach
    public void closeManager() {
        entityManager.close();
    }

    @AfterAll
    public static void closeFactory() {
        entityManagerFactory.close();
    }

    @Test
    public void 엔티티매니저팩토리와엔티티매니저생명주기확인1() {
        System.out.println("entityManagerFactory.hashCode1: " + entityManagerFactory.hashCode());
        System.out.println("entityManager.hashCode1: " + entityManager.hashCode());
    }

    @Test
    public void 엔티티매니저팩토리와엔티티매니저생명주기확인2() {
        System.out.println("entityManagerFactory.hashCode2: " + entityManagerFactory.hashCode());
        System.out.println("entityManager.hashCode2: " + entityManager.hashCode());
    }
}