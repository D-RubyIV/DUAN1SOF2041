/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raven.utils;

import com.raven.entity.NguoiDung;

/**
 *
 * @author phamh
 */
public class Auth {
    public static NguoiDung user = null;
    public static void clear(){
        Auth.user = null;
    }
    public static boolean isLogin(){
        return Auth.user != null;
    }
    public static boolean isManager(){
        return Auth.isLogin() && user.getMaVaiTro() == 1;
    }
    public static boolean isCustomer(){
        return Auth.isLogin() && user.getMaVaiTro() == 3;
    }
    public static boolean isStaff(){
        return Auth.isLogin() && user.getMaVaiTro() == 2;
    }
    
}
