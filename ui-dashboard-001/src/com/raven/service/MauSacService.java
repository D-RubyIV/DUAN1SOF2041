/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raven.service;

/**
 *
 * @author phamh
 */

import com.ravent.database.DBContext;
import com.ravent.entity.MauSac;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class MauSacService {

    public MauSac findById(int maMauSac) {
        String sql = "SELECT * FROM MAUSAC WHERE MAMAUSAC = " + maMauSac;
//        System.out.println(sql);
        try {
            Statement statement = new DBContext().getConnect().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int maMau = rs.getInt("MAMAUSAC");
                String tenMau = rs.getString("TENMAUSAC");
                MauSac mauSac = new MauSac(maMau, tenMau);
                return mauSac;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<MauSac> selectAll() {
        List<MauSac> listMauSac = new ArrayList<>();
        String sql = "SELECT * FROM MAUSAC";
        try {
            Statement st = new DBContext().getConnect().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                int maMau = rs.getInt("MAMAUSAC");
                String tenMau = rs.getString("TENMAUSAC");
                listMauSac.add(new MauSac(maMau, tenMau));
            }
        } catch (Exception e) {
            System.out.println("MAUSAC SERVICE ERROR SELECT ALL:" + e);
        }
        return listMauSac;
    }

    public String add(String tenMauSac) {
        Connection con = new DBContext().getConnect();
        String sql = "INSERT INTO MAUSAC (TENMAUSAC) VALUES (?)";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, tenMauSac);
            int result = st.executeUpdate();
            if (result > 0) {
                return "Thêm Thành Công";
            }
            return "Thêm Thất Bại";
        } catch (Exception e) {
            return "Thêm Lỗi: " + e;
        }
    }

    public String delete(int maMauSac) {
        Connection con = new DBContext().getConnect();
        String sql = "DELETE FROM MAUSAC WHERE MAMAUSAC = ?";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, maMauSac);
            int result = st.executeUpdate();
            if (result > 0) {
                return "Xóa Thành Công";
            }
            return "Xóa Thất Bại";
        } catch (Exception e) {
            if(e.toString().contains("FK")){
                return "Xóa Lỗi: Tồn Tại Khóa Ngoại";
            }
            return "Xóa Lỗi: " + e;
        }
    }

    public String update(MauSac mauSac) {
        Connection con = new DBContext().getConnect();
        String sql = "UPDATE MAUSAC SET TENMAUSAC=? WHERE MAMAUSAC = ?";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, mauSac.getTenMauSac());
            st.setInt(2, mauSac.getMaMauSac());

            int result = st.executeUpdate();
            if (result > 0) {
                return "Update Thành Công";
            }
            return "Update Thất Bại";
        } catch (Exception e) {
            return "Update Lỗi: " + e;
        }
    }
}