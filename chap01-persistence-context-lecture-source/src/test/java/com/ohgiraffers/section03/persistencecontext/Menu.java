
package com.ohgiraffers.section03.persistencecontext;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name="section03_menu")  // 하나의 설정에 Entity명이 겹치면 안 된다.
@Table(name="tbl_menu")         // 이 Entity는 곧 이 Table이 된다는 설정
public class Menu {
    public static Menu menuToDetach;             // @Entity를 쓰자마자 Class명에 error가 생기는데 이는 pk가 될 속성을 설정을 해줘야 하기 때문이다 -> @Id


    @Id
    @Column(name="menu_code")
    /* 설명. Entity 객체를 만들 때 값을 넣지 않거나(DB의 auto_increment로 따르게)
         auto ddl을 통해 Entity로 테이블 생성 시 활용할 목적 */
//    @GeneratedValue(strategy=GenerationType.IDENTITY)   // auto_increment 활용
    private int menuCode;   // pk가 됨

    @Column(name="menu_name")
    private String menuName;

    @Column(name="menu_price")
    private int menuPrice;

    @Column(name="category_code")
    private int categoryCode;

    @Column(name="orderable_status")
    private String orderableStatus;

    public Menu() {
    }
    public Menu(int menuCode, String menuName, int menuPrice, int categoryCode, String orderableStatus) {
        this.menuCode = menuCode;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.categoryCode = categoryCode;
        this.orderableStatus = orderableStatus;
    }

    public int getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(int menuCode) {
        this.menuCode = menuCode;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(int menuPrice) {
        this.menuPrice = menuPrice;
    }

    public int getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(int categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getOrderableStatus() {
        return orderableStatus;
    }

    public void setOrderableStatus(String orderableStatus) {
        this.orderableStatus = orderableStatus;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "menuCode=" + menuCode +
                ", menuName='" + menuName + '\'' +
                ", menuPrice=" + menuPrice +
                ", categoryCode=" + categoryCode +
                ", orderableStatus='" + orderableStatus + '\'' +
                '}';
    }
}
