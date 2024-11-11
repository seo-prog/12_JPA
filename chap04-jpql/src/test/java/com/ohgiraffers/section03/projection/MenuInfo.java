package com.ohgiraffers.section03.projection;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class MenuInfo {
    // 복합키
    // 복합키는 기본키가 2가지인 게 복합키인데, 임베디드에 넣을 때 id 가 아니여도 설정이 가능하다.(임베디드일때만 ! 일반적인 sql 의 걔념과 다름)
    // 둘 다 같은건 없어야 한대.. 예를들어 2개가 쌍으로 같은건 안된다. 조합이 유일해야 한대.
    // 여기만 일케 쓴다고 db 에 바로 되는게 아니라 따로 테이블 조정을 해줘야 한다. (unique 라던지..)
    // 즉, 원래 일케 복합키로 쓰려면 테이블 설계도 해줘야 한다.

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "menu_price")
    private int menuPrice;

    public MenuInfo() {
    }

    public MenuInfo(String menuName, int menuPrice) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
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

    @Override
    public String toString() {
        return "MenuInfo{" +
                "menuName='" + menuName + '\'' +
                ", menuPrice=" + menuPrice +
                '}';
    }
}
