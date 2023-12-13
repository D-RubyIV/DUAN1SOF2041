package com.raven.model;

import java.util.Objects;
import java.util.Optional;

public class Product {

    private Optional<String> pname;
    private String tenHang;
    private String tenSize;
    private String tenChatLieu;
    private String tenMauSac;
    private int quantity;

    private float priceperpeice;

    public Product(String pname, String tenHang, String tenSize, String tenChatLieu, String tenMauSac, int quantity, float priceperpeice) {
        this.pname = Optional.ofNullable(pname);
        this.tenHang = tenHang;
        this.tenSize = tenSize;
        this.tenChatLieu = tenChatLieu;
        this.tenMauSac = tenMauSac;
        this.quantity = quantity;
        this.priceperpeice = priceperpeice;
    }

 
  
    public Optional<String> getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = Optional.ofNullable(pname);
    }

    public String getTenHang() {
        return tenHang;
    }

    public void setTenHang(String tenHang) {
        this.tenHang = tenHang;
    }

    public String getTenSize() {
        return tenSize;
    }

    public void setTenSize(String tenSize) {
        this.tenSize = tenSize;
    }

    public String getTenChatLieu() {
        return tenChatLieu;
    }

    public void setTenChatLieu(String tenChatLieu) {
        this.tenChatLieu = tenChatLieu;
    }

    public String getTenMauSac() {
        return tenMauSac;
    }

    public void setTenMauSac(String tenMauSac) {
        this.tenMauSac = tenMauSac;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPriceperpeice() {
        return priceperpeice;
    }

    public void setPriceperpeice(float priceperpeice) {
        this.priceperpeice = priceperpeice;
    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (!(o instanceof Product)) {
//            return false;
//        }
//        Product product = (Product) o;
//        return Objects.equals(pname, product.pname);
//    }

    @Override
    public int hashCode() {
        return Objects.hash(pname);
    }

    @Override
    public String toString() {
        return "{"
                + "pname=" + pname
                + ", quantity=" + quantity
                + ", priceperpeice=" + priceperpeice
                + '}';
    }
}
