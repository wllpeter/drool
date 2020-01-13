package com.example.demo.modal;

/**
 * Created by 86131 on 2020/1/13.
 */
public class Product {
    public static final String DIAMOND = "DIAMOND"; // 钻石
    public static final String GOLD = "GOLD"; // 黄金
    private String type;
    private int discount;
    private int money;

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}

