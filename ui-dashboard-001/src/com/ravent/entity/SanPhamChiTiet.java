/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ravent.entity;

/**
 *
 * @author phamh
 */
public class SanPhamChiTiet {
    private int maSanPhamChiTiet;
    private int maSanPham;
    private int maHang;
    private int maMauSac;
    private int maSize;
    private int maChatLieu;
    private int soLuong;
    private float giaSanPham;
    private String mota;
    private String hinhAnh;

    public SanPhamChiTiet(int maSanPhamChiTiet, int maSanPham, int maHang, int maMauSac, int maSize, int maChatLieu, int soLuong, float giaSanPham, String mota, String hinhAnh) {
        this.maSanPhamChiTiet = maSanPhamChiTiet;
        this.maSanPham = maSanPham;
        this.maHang = maHang;
        this.maMauSac = maMauSac;
        this.maSize = maSize;
        this.maChatLieu = maChatLieu;
        this.soLuong = soLuong;
        this.giaSanPham = giaSanPham;
        this.mota = mota;
        this.hinhAnh = hinhAnh;
    }

    public SanPhamChiTiet() {
    }

    public int getMaSanPhamChiTiet() {
        return maSanPhamChiTiet;
    }

    public void setMaSanPhamChiTiet(int maSanPhamChiTiet) {
        this.maSanPhamChiTiet = maSanPhamChiTiet;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public int getMaHang() {
        return maHang;
    }

    public void setMaHang(int maHang) {
        this.maHang = maHang;
    }

    public int getMaMauSac() {
        return maMauSac;
    }

    public void setMaMauSac(int maMauSac) {
        this.maMauSac = maMauSac;
    }

    public int getMaSize() {
        return maSize;
    }

    public void setMaSize(int maSize) {
        this.maSize = maSize;
    }

    public int getMaChatLieu() {
        return maChatLieu;
    }

    public void setMaChatLieu(int maChatLieu) {
        this.maChatLieu = maChatLieu;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public float getGiaSanPham() {
        return giaSanPham;
    }

    public void setGiaSanPham(float giaSanPham) {
        this.giaSanPham = giaSanPham;
    }

    public String getMota() {
        return mota;
    }

    @Override
    public String toString() {
        return "SanPhamChiTiet{" + "maSanPhamChiTiet=" + maSanPhamChiTiet + ", maSanPham=" + maSanPham + ", maHang=" + maHang + ", maMauSac=" + maMauSac + ", maSize=" + maSize + ", maChatLieu=" + maChatLieu + ", soLuong=" + soLuong + ", giaSanPham=" + giaSanPham + ", mota=" + mota + ", hinhAnh=" + hinhAnh + '}';
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    
    
    
    
    
    
    

}
