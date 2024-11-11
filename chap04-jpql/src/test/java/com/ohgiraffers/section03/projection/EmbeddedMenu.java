package com.ohgiraffers.section03.projection;


import jakarta.persistence.*;

@Entity(name = "embedded_menu")
@Table(name = "tbl_menu")
public class EmbeddedMenu {

    @Id
    @Column(name = "menu_code")
    private int menuCode;

    @Embedded
    private MenuInfo menuInfo;

    @Column(name = "category_code")
    private int categoryCode;

    @Column(name = "orderable_status")
    private String orderableStatus;

    public EmbeddedMenu() {
    }

    public EmbeddedMenu(int menuCode, MenuInfo menuInfo, int categoryCode, String orderableStatus) {
        this.menuCode = menuCode;
        this.menuInfo = menuInfo;
        this.categoryCode = categoryCode;
        this.orderableStatus = orderableStatus;
    }

    public int getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(int menuCode) {
        this.menuCode = menuCode;
    }

    public MenuInfo getMenuInfo() {
        return menuInfo;
    }

    public void setMenuInfo(MenuInfo menuInfo) {
        this.menuInfo = menuInfo;
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
        return "EmbeddedMenu{" +
                "menuCode=" + menuCode +
                ", menuInfo=" + menuInfo +
                ", categoryCode=" + categoryCode +
                ", orderableStatus='" + orderableStatus + '\'' +
                '}';
    }
}
