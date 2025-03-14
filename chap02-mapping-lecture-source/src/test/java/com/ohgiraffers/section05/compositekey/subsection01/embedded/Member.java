package com.ohgiraffers.section05.compositekey.subsection01.embedded;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

@Entity
public class Member {

    @EmbeddedId /*Embeddable타입으로 만든? 복합키*/
    private MemberPK memberPk;
    private String phone;
    private String address;

}
