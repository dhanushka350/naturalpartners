package com.akvasoft.natural_partners;

import javax.persistence.*;

@Entity
@Table(name = "T_PRODUCT")
public class Modal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "T_PRODUCT_ID")
    private int id;

    @Column(name = "T_PRODUCT_CATEGORY")
    private String category;

    @Column(name = "T_PRODUCT_TITLE")
    private String title;

    @Column(name = "T_PRODUCT_NAME")
    private String name;

    @Column(name = "T_PRODUCT_CODE")
    private String code;

    @Column(name = "T_PRODUCT_PRICE")
    private String price;

    @Column(name = "T_PRODUCT_DESCRIPTION")
    private String desc;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
