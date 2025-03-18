package com.ohgiraffers.springdatajpa.menu.repository;

import com.ohgiraffers.springdatajpa.menu.entity.Menu;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/* 알아서 하위구현체를 만들어줌. 그리고 그 객체는 콩이 되고 의존성 주입을 받을 수 있음. */
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    List<Menu> findByMenuPriceGreaterThan(int menuPrice);


}

