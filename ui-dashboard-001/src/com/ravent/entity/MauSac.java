/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ravent.entity;

/**
 *
 * @author phamh
 */
public class MauSac {
    private int maMauSac;
    private String tenMauSac;

    @Override
    public String toString() {
        return tenMauSac;
    }

    public int getMaMauSac() {
        return maMauSac;
    }

    public void setMaMauSac(int maMauSac) {
        this.maMauSac = maMauSac;
    }

    public String getTenMauSac() {
        return tenMauSac;
    }

    public void setTenMauSac(String tenMauSac) {
        this.tenMauSac = tenMauSac;
    }

    public MauSac() {
    }

    public MauSac(int maMauSac, String tenMauSac) {
        this.maMauSac = maMauSac;
        this.tenMauSac = tenMauSac;
    }

}
