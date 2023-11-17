/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ravent.entity;

/**
 *
 * @author phamh
 */
public class Hang {
    private int maHang;
    private String tenHang;

    public Hang(int maHang, String tenHang) {
        this.maHang = maHang;
        this.tenHang = tenHang;
    }

    public Hang() {
    }

    public int getMaHang() {
        return maHang;
    }

    public void setMaHang(int maHang) {
        this.maHang = maHang;
    }

    public String getTenHang() {
        return tenHang;
    }

    public void setTenHang(String tenHang) {
        this.tenHang = tenHang;
    }

    @Override
    public String toString() {
        return tenHang;
    }
    
    
    
}
