/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raven.entity;

/**
 *
 * @author phamh
 */
public class HoaDonChiTiet {
    private int maHoaDonChiTiet;
    private int maSanPhamChiTiet;
    private int maHoaDon;
    private int soLuong;

    public HoaDonChiTiet(int maHoaDonChiTiet, int maSanPhamChiTiet, int maHoaDon, int soLuong) {
        this.maHoaDonChiTiet = maHoaDonChiTiet;
        this.maSanPhamChiTiet = maSanPhamChiTiet;
        this.maHoaDon = maHoaDon;
        this.soLuong = soLuong;
    }

    public HoaDonChiTiet() {
    }

    public int getMaHoaDonChiTiet() {
        return maHoaDonChiTiet;
    }

    public void setMaHoaDonChiTiet(int maHoaDonChiTiet) {
        this.maHoaDonChiTiet = maHoaDonChiTiet;
    }

    public int getMaSanPhamChiTiet() {
        return maSanPhamChiTiet;
    }

    public void setMaSanPhamChiTiet(int maSanPhamChiTiet) {
        this.maSanPhamChiTiet = maSanPhamChiTiet;
    }

    public int getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(int maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    @Override
    public String toString() {
        return "HoaDonChiTiet{" + "maHoaDonChiTiet=" + maHoaDonChiTiet + ", maSanPhamChiTiet=" + maSanPhamChiTiet + ", maHoaDon=" + maHoaDon + ", soLuong=" + soLuong + '}';
    }

  
    
}
