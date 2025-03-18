package com.ohgiraffers.springdatajpa.menu.controller;


import com.ohgiraffers.springdatajpa.common.Pagination;
import com.ohgiraffers.springdatajpa.common.PagingButtonInfo;
import com.ohgiraffers.springdatajpa.menu.dto.CategoryDTO;
import com.ohgiraffers.springdatajpa.menu.dto.MenuDTO;
import com.ohgiraffers.springdatajpa.menu.repository.MenuRepository;
import com.ohgiraffers.springdatajpa.menu.service.MenuService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/menu")
@Slf4j /*변수지정하지 않아도 로그 사용 가능*/
public class MenuController {

    private final MenuService menuService;
    private final MenuRepository menuRepository;
    private final PageableHandlerMethodArgumentResolver pageableResolver;

    /* 설명.
     *  Logger를 활용한 로그 생성 이유?
     *  1. println보다 성능적으로 우수하다.ㅣ
     *  2. 외부 리소스 파일로 따로 저장 및 송출이 가능하다.
     *  3. 로그레벨에 따른 확인이 가능하다.(개발: debug, 서비스: info)  */

//    Logger logger = LoggerFactory.getLogger(MenuController.class);
//    Logger logger = LoggerFactory.getLogger(getClass());


    public MenuController(MenuService menuService, MenuRepository menuRepository,
                          PageableHandlerMethodArgumentResolver pageableResolver) {
        this.menuService = menuService;
        this.menuRepository = menuRepository;
        this.pageableResolver = pageableResolver;
    }

    @GetMapping("/{menuCode}")
    public String findMenuByCode(@PathVariable int menuCode, Model model) {
//        System.out.println("menuCode = " + menuCode);
//        logger.debug("menuCode = {}", menuCode);
        log.debug("menuCode = {}", menuCode);

        MenuDTO menu = menuService.findMenuByCode(menuCode);

        model.addAttribute("menu", menu);

        return "menu/detail";
    }

    /* 설명. 페이징 처리 전 */
//    @GetMapping("/list")
//    public String findMenuList(Model model) {
//        List<MenuDTO> menuList = menuService.findMenuList();
//
//        model.addAttribute("menuList", menuList);
//
//        return "menu/list";
//    }

    /* 설명. 페이징 처리 후 */
    /* 설명.
     *  @PageableDefault
     *   1. 기본 한 페이지에 10개의 데이터(size, value)
     *   2. 기본 1페이지부터 (0부터)
     *   3. 기본 오름차순 (ASC) */
    @GetMapping("/list")
    public String findMenuList(@PageableDefault Pageable pageable, Model model) {
        log.debug("pageable: {}", pageable);

        Page<MenuDTO> menuList = menuService.findMenuList(pageable); /*뿌릴 재료 내용?*/

        log.debug("조회한 내용 목록: {}", menuList.getContent());
        log.debug("총 페이지 수 : {}", menuList.getTotalPages());
        log.debug("총 메뉴 수 : {}", menuList.getTotalElements());
        log.debug("해당 페이지에 표시 될 요소 수 : {}", menuList.getSize());
        log.debug("해당 페이지에 실제 요소 수 : {}", menuList.getNumberOfElements());
        log.debug("Page의 number가 처음이면(첫페이지면): {}", menuList.isFirst());
        log.debug("Page의 number가 마지막이면(마지막 페이지면): {}", menuList.isLast());
        log.debug("현재 페이지: {}", menuList.getNumber());
        log.debug("정렬 기준: {}", menuList.getSort());

        /* 설명. Page객체를 통해 PagingButtonInfo 추출 */
        PagingButtonInfo paging = Pagination.getPagingButtonInfo(menuList); /* 버튼 내용? */

        model.addAttribute("menuList", menuList);
        model.addAttribute("paging", paging);

        return "menu/list";
    }

    @GetMapping("querymethod")
    public void queryMethodPage() {

    }

    @GetMapping("search")
    public String findMenuPrice(@RequestParam int menuPrice, Model model) {
        List<MenuDTO> menuList = menuService.findMenuPrice(menuPrice);

        model.addAttribute("menuList", menuList);
        model.addAttribute("menuPrice", menuPrice);
        return "menu/searchResult";
    }

    @GetMapping("regist")
    public void registMenuPage() {
    }

    @GetMapping("category")
    @ResponseBody
    public List<CategoryDTO> findCategoryList() {
        return menuService.findAllCategory();
    }

    @PostMapping("regist")
    public String registMenu(MenuDTO newMenu) {
//        log.debug("컨트롤러에서 커맨드 객체로 한번에 입력값 잘 받는지 확인: {}/**/", newMenu);
        /* memo: 추가한 메뉴를 바로 보여질 수 있도록 redirect*/

        menuService.registMenu(newMenu);

        return "redirect:/menu/list";
    }

    @GetMapping("modify")
    public void modifyMenuPage() {
    }

    @PostMapping("modify")
    public String modifyMenu(MenuDTO modifyMenu) {
        menuService.modifyMenu(modifyMenu);

        return "redirect:/menu/" + modifyMenu.getMenuCode();
    }

    @GetMapping("delete")
    public void deleteMenuPage() {
    }

    @PostMapping("delete")
    public String deleteMenu(@RequestParam int menuCode) {
        menuService.deleteMenu(menuCode);

        return "redirect: /menu/list";
    }
}
