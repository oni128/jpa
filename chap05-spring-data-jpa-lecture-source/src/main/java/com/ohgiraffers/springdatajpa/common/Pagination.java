package com.ohgiraffers.springdatajpa.common;

import org.springframework.data.domain.Page;

/* 설명. Page 타입이 넘어오면 PagingButtonInfo를 반환하는 paging 기능 모듈 */
public class Pagination {
    public static PagingButtonInfo getPagingButtonInfo(Page page) {
        int currentPage = page.getNumber() + 1;     // 인덱스 개념 -> 실제 페이지 번호의 개념으로 다시 변경
        int defaultButtonCount = 10;                // 한 페이지에 보일 버튼 최대 갯수;
        int startPage;
        int endPage;

        startPage = (int)(Math.ceil((double) currentPage / defaultButtonCount) - 1)
                * defaultButtonCount + 1;
        // ex) 12페이지 일때, 12 -> 1.2 -> 2 -> 1 -> 10-> 11
        endPage = startPage + defaultButtonCount - 1;

        if (page.getTotalPages() < endPage) {       // totalPage가 마지막 페이지보다 작으면
            endPage = page.getTotalPages();         // totalPage가 마지막 페이지 버튼이 된다.
        }

        if (page.getTotalPages() == 0) {            // 아예 화면에 보일 내용이 없으면 (1페이지도 안된다면)
            endPage = startPage;                    // startPage와 endPage가 같다. (1페이지만 표시)
        }

        return new PagingButtonInfo(currentPage, startPage, endPage);

    }
}
