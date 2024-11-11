package com.ohgiraffers.section01.manytoone;

/*
* @JoinColumn 은 다대일 연관 관계에서 사용된는 어노테이션이다.
* 다음과 같은 속성들을 사용할 수 있다.
*
* - name : 참조하는 테이블의 컬럼명을 지정한다,
* - referencedColumnName : 참조되는 컬럼명을 지정한다.( default 는 pk )
* - nullable : 참조하는 테이블의 컬럼에 null 값을 허용할 지 여부를 지정
* - unique : 참조하는 테이블의 컬럼에 유일성 제약 조건을 추가할지 여부를 지정
* - insertable : 새로운 엔티티가 저장될 때, 이 참조 컬럼이 sql insert 문에 포함될지 지정
* - updatable : 엔티티가 업데이트 될 뗴, 이 참조 컬럼이 update 문에 포함될지 지정
* - table : 참조하는 테이블의 이름을 지정
* - foreignKey : 참조하는 테이블에 생성될 외래 키에 대한 추가 정보를 지정
*
* @ManyToOne 은 다대일 연관 관계에서 사용하는 어노테이션이다.
* 다음과 같은 속성을 사용할 수 있다.
* - cascade : 연관된 엔티티에 대한 영속성 전이를 설정한다.
* - fetch : 연관된 엔티티를 로딩하는 전략을 설정한다.
* - optional : 연관된 엔티티가 필수인지 선택적인지 설정한다.
* */

import jakarta.persistence.*;

@Entity(name = "menu_and_category")
@Table(name = "tbl_menu")
public class MenuAndCategory {

    @Id
    @Column(name = "menu_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int menuCode;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "menu_price")
    private int menuPrice;

    /*
    * CascadeType
    * - PERSIST : 자식의 값이 저장될 때 연관관계를 가지고 있는 부모의 값도 함꼐 저장됨.
    * - REMOVE : 엔티티를 제거할 때 연관된 엔티티도 모두 제거한다.
    * - MERGE : 엔티티 상태를 병합할 때 연관된 하위 엔티티도 모두 병합한다.
    * - DETACH : 엔티티를 DETACH() 하면 연관 엔티티도 DETACH 상태가 된다.
    * */

    @JoinColumn(name = "category_code")
    // @ManyToOne(cascade = CascadeType.PERSIST) // 이걸 하면 부모도 자동으로 등록을 한다.
    // @ManyToOne(cascade = CascadeType.REMOVE)
    @ManyToOne(cascade = CascadeType.DETACH)
    private Category category;

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
                ", orderableStatue='" + orderableStatus + '\'' +
                '}';
    }
}
