package com.ohgiraffers.section03.bidirection;

import com.ohgiraffers.section01.manytoone.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import javax.xml.namespace.QName;

/*카테고리와 조인할 수 있는 MENU 엔티티*/
@Entity(name = "bidirection_menu")
@Table(name = "tbl_menu")
public class MenuAndCategory { /* 테이블의 개념 + join */

    @Id
    @Column(name = "menu_code")
    private int menuCode;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "menu_Price")
    private int menuPrice;


    @ManyToOne   /*카테고리 말고 순수 메뉴꺼만 보고싶어 너는 내가 찾을때 가져와 , menu카테고리 없이 join도 쓸 수 없음.*/
    // 메뉴가 카테고리를 볼 때 전체 카디널리티(해당되는 행의 매핑 개수)  (어떻게 관계를 맺는지?)
    @JoinColumn(name = "category_code") // 여기 컬럼명은 FK제약조건이 있는 컬럼명(자식테이블에 있는 컬럼명만 쓴다.)
    private Category category; /* 단방향 */ // 메뉴 1개가 카테고리 엔티티 객체를 몇개 가지는지(List<타입>/타입) 한개만 가지므로 타입만 적음.


    @Column(name = "orderable_status")
    private String orderableStatus;

    public MenuAndCategory() {
    }

    public MenuAndCategory(int menuCode, String menuName, int menuPrice, Category category, String orderableStatus) {
        this.menuCode = menuCode;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.category = category;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getOrderableStatus() {
        return orderableStatus;
    }

    public void setOrderableStatus(String orderableStatus) {
        this.orderableStatus = orderableStatus;
    }

    @Override
    public String toString() {
        return "MenuAndCategory{" +
                "menuCode=" + menuCode +
                ", menuName='" + menuName + '\'' +
                ", menuPrice=" + menuPrice +
                ", category=" + category +
                ", orderableStatus='" + orderableStatus + '\'' +
                '}';
    }
}
