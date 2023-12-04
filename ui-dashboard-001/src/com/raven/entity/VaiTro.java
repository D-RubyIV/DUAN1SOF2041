/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raven.entity;

/**
 *
 * @author asub
 */
public class VaiTro {

    private Integer maVaitro;
    private String tenVaitro;

    public VaiTro(Integer maVaitro, String tenVaitro) {
        this.maVaitro = maVaitro;
        this.tenVaitro = tenVaitro;
    }

    public VaiTro() {
    }

    public Integer getMaVaitro() {
        return maVaitro;
    }

    public void setMaVaitro(Integer maVaitro) {
        this.maVaitro = maVaitro;
    }

    public String getTenVaitro() {
        return tenVaitro;
    }

    public void setTenVaitro(String tenVaitro) {
        this.tenVaitro = tenVaitro;
    }

    @Override
    public String toString() {
        return tenVaitro;
    }
    

}
