/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raven.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 *
 * @author phamh
 */
@AllArgsConstructor
@NoArgsConstructor
public class DuLieuThongKeHoaDon {
    private Date date;
    private int huyThanhToan;
    private int daThanhToan;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getHuyThanhToan() {
        return huyThanhToan;
    }

    public void setHuyThanhToan(int huyThanhToan) {
        this.huyThanhToan = huyThanhToan;
    }

    public int getDaThanhToan() {
        return daThanhToan;
    }

    public void setDaThanhToan(int daThanhToan) {
        this.daThanhToan = daThanhToan;
    }
    
    
    
}
