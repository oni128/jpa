package com.ohgiraffers.section03.persistencecontext;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;
public class EntityLifeCycleTest {
    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @BeforeAll
    public static void initFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

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

    /* 설명.
     *  영속성 컨텍스트는 엔티티 매니저가 엔티티 객체를 저장하는 공간으로 엔티티 객체를 보관하고 관리한다.
     *  엔티티 매니저가 생성될 때 하나의 영속성 컨텍스트가 할당된다.
     *
     *  설명.
     *    엔티티의 생명 주기
     *    비영속, 영속, 준영속, 삭제 상태
     * */

    @Test
    public void 비영속성_테스트() {
        Menu foundMenu = entityManager.find(Menu.class, 11); /* 영속 상태?*/

        /* 설명. 비영속 상태의 newMenu */
        Menu newMenu = new Menu();
        newMenu.setMenuCode(foundMenu.getMenuCode());
        newMenu.setMenuName(foundMenu.getMenuName());
        newMenu.setMenuPrice(foundMenu.getMenuPrice());
        newMenu.setCategoryCode(foundMenu.getCategoryCode());
        newMenu.setOrderableStatus(foundMenu.getOrderableStatus());

        Assertions.assertFalse(foundMenu == newMenu); // 동등하지만 동일하지는 않다.
    }

    @Test
    public void 영속성_테스트() {
        /* 영속끼리는 주소?가 같음 */
        Menu foundMenu1 = entityManager.find(Menu.class, 11);
        Menu foundMenu2 = entityManager.find(Menu.class, 11);

        Assertions.assertTrue(foundMenu1 == foundMenu2);
    }

    @Test
    public void 영속성_객체_추가_테스트() {
        /* 설명. 이 예제에서는  @Id에 해당하는 속성에 직접 값을 넣기 위해 @GeneratedValue를 주석 처리할 것! */
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Menu menuToRegist = new Menu();
        menuToRegist.setMenuCode(500);
        menuToRegist.setMenuName("수박죽");
        menuToRegist.setMenuPrice(100000);
        menuToRegist.setCategoryCode(10);
        menuToRegist.setOrderableStatus("Y");

        entityManager.persist(menuToRegist); /* entityManager에게 인지시키기 */
        Menu foundMenu = entityManager.find(Menu.class, 500);

        transaction.commit();

        Assertions.assertTrue(menuToRegist == foundMenu);
    }

    @Test
    public void 영속성_객체_추가_및_변경_테스트() {
        EntityTransaction transaction= entityManager.getTransaction();
        transaction.begin();

        /*수정하려면 find, persist*/
        Menu menuToRegist = new Menu(); /*영속상태 수정하면 update? */
        menuToRegist.setMenuCode(501);
        menuToRegist.setMenuName("수박죽");
        menuToRegist.setMenuPrice(100000);
        menuToRegist.setCategoryCode(10);
        menuToRegist.setOrderableStatus("Y");

        entityManager.persist(menuToRegist); // 여기서부터 menuToRegist는 '영속'상태가 됨
        menuToRegist.setMenuName("메론죽");

        Menu foundMenu = entityManager.find(Menu.class, 501);

        transaction.commit();

        assertEquals("메론죽", foundMenu.getMenuName());
    }
    
    @Test
    public void 준영속성_detach_테스트() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Menu foundMenu1 = entityManager.find(Menu.class, 11);
        Menu foundMenu2 = entityManager.find(Menu.class, 12);
        System.out.println("foundMenu1 = " + foundMenu1);
        System.out.println("foundMenu2 = " + foundMenu2);

        /* 설명.
         *  영속성 컨텐스트가 관리하던 엔티티 객체를 잠시 관리하지 않는 상태가 되게 한 것을 준영속 상태라고 한다.
         *  detach, clear, close가 준영속 상태를 만들기 위한 메소드이다.
         *
         *
         * 설명.
         *  준영속을 사용하는 이유?
         *  JPA에서 엔티티를 더 이상 영속성 컨텍스트에서 관리하지 않음을 의미하며, 특정 상황에서 성능 최적화나
         *  데이터 무결성 유지, 특정 작업 후 엔티티 변경 방지를 위해 사용된다.
         * */

        entityManager.detach(foundMenu2);

        foundMenu1.setMenuPrice(7000);
        foundMenu2.setMenuPrice(70000);

        transaction.commit();

        assertEquals(7000, entityManager.find(Menu.class, 11).getMenuPrice());
        assertNotEquals(7000, entityManager.find(Menu.class, 12).getMenuPrice());

    }

    @Test
    public void 준영속성_clear_close_테스트() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Menu foundMenu1 = entityManager.find(Menu.class, 11);
        Menu foundMenu2 = entityManager.find(Menu.class, 12);

//        entityManager.clear(); // 영속성 컨텍스트에 있는 모든 영속 상태의 엔티티를 준영속 상태로 변경
        /* 둘다 준영속으로 만듦 */


        entityManager.clear();  // 기존의 영속상태의 엔티티들이 모두 준영속 상태로 변경 + 영속성 컨텍스트 파괴
        entityManager = entityManagerFactory.createEntityManager(); // 폐점시키고 다시 새로 개업함. merge 먄 쓰던거 쓸 수 있음.
        foundMenu1.setMenuPrice(7500);
        foundMenu2.setMenuPrice(7500);

        transaction.commit();

        assertNotEquals(7500, entityManager.find(Menu.class, 11).getMenuPrice());
        assertNotEquals(7500, entityManager.find(Menu.class, 12).getMenuPrice());
    }

    @Test
    public void 병합_merge_수정_테스트() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Menu menuToDetach = entityManager.find(Menu.class, 5);
        entityManager.close();// 모든 영속적 엔티티가 준영속으로

        menuToDetach.setMenuName("수박죽");

        Menu refounMenu = entityManager.find(Menu.class, 5);
        System.out.println("첫 번째 주소값: " + menuToDetach.hashCode());
        System.out.println("두 번째 주소값: " + refounMenu.hashCode());

        entityManager.merge(menuToDetach);

        Menu manageMenu = entityManager.find(Menu.class, 5);

        transaction.commit();

        /* 수박죽이 맞는지 반정문으로 씀 */
        assertEquals("수박죽", manageMenu.getMenuName());

    }
}
