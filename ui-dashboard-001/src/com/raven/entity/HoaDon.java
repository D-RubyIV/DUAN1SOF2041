/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raven.entity;

import java.util.Date;

/**
 *
 * @author phamh
 */
public class HoaDon {
    private int maHoaDon;
    private int maNguoiBan;
    private int maKhachHang;
    private String maGiamGia;
    private Date thoiGianTao;
    private Date thoiGianThanhToan;
    private float tongTienTruocGiamGia;
    private float tongTienSauGiamGia;
    private float tongTienGiamGia;
    private int trangThaiThanhToan;

    public HoaDon(int maHoaDon, int maNguoiBan, int maKhachHang, String maGiamGia, Date thoiGianTao, Date thoiGianThanhToan, float tongTienTruocGiamGia, float tongTienSauGiamGia, float tongTienGiamGia, int trangThaiThanhToan) {
        this.maHoaDon = maHoaDon;
        this.maNguoiBan = maNguoiBan;
        this.maKhachHang = maKhachHang;
        this.maGiamGia = maGiamGia;
        this.thoiGianTao = thoiGianTao;
        this.thoiGianThanhToan = thoiGianThanhToan;
        this.tongTienTruocGiamGia = tongTienTruocGiamGia;
        this.tongTienSauGiamGia = tongTienSauGiamGia;
        this.tongTienGiamGia = tongTienGiamGia;
        this.trangThaiThanhToan = trangThaiThanhToan;
    }

    public HoaDon() {
    }

    public int getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(int maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public int getMaNguoiBan() {
        return maNguoiBan;
    }

    public void setMaNguoiBan(int maNguoiBan) {
        this.maNguoiBan = maNguoiBan;
    }

    public int getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(int maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getMaGiamGia() {
        return maGiamGia;
    }

    public void setMaGiamGia(String maGiamGia) {
        this.maGiamGia = maGiamGia;
    }

    public Date getThoiGianTao() {
        return thoiGianTao;
    }

    public void setThoiGianTao(Date thoiGianTao) {
        this.thoiGianTao = thoiGianTao;
    }

    public Date getThoiGianThanhToan() {
        return thoiGianThanhToan;
    }

    @Override
    public String toString() {
        return "HoaDon{" + "maHoaDon=" + maHoaDon + ", maNguoiBan=" + maNguoiBan + ", maKhachHang=" + maKhachHang + ", maGiamGia=" + maGiamGia + ", thoiGianTao=" + thoiGianTao + ", thoiGianThanhToan=" + thoiGianThanhToan + ", tongTienTruocGiamGia=" + tongTienTruocGiamGia + ", tongTienSauGiamGia=" + tongTienSauGiamGia + ", tongTienGiamGia=" + tongTienGiamGia + ", trangThaiThanhToan=" + trangThaiThanhToan + '}';
    }

    public void setThoiGianThanhToan(Date thoiGianThanhToan) {
        this.thoiGianThanhToan = thoiGianThanhToan;
    }

    public float getTongTienTruocGiamGia() {
        return tongTienTruocGiamGia;
    }

    public void setTongTienTruocGiamGia(float tongTienTruocGiamGia) {
        this.tongTienTruocGiamGia = tongTienTruocGiamGia;
    }

    public float getTongTienSauGiamGia() {
        return tongTienSauGiamGia;
    }

    public void setTongTienSauGiamGia(float tongTienSauGiamGia) {
        this.tongTienSauGiamGia = tongTienSauGiamGia;
    }

    public float getTongTienGiamGia() {
        return tongTienGiamGia;
    }

    public void setTongTienGiamGia(float tongTienGiamGia) {
        this.tongTienGiamGia = tongTienGiamGia;
    }

    public int getTrangThaiThanhToan() {
        return trangThaiThanhToan;
    }

    public void setTrangThaiThanhToan(int trangThaiThanhToan) {
        this.trangThaiThanhToan = trangThaiThanhToan;
    }

   
    
}
