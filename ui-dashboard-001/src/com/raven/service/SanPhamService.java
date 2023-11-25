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
import com.ravent.entity.SanPham;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class SanPhamService {

    public SanPham findById(int maSanPham) {
        String sql = "SELECT * FROM SANPHAM WHERE MASANPHAM = " + maSanPham;
        try {
            Statement statement = new DBContext().getConnect().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int maSp = rs.getInt("MASANPHAM");
                String tenSp = rs.getString("TENSANPHAM");
                SanPham sanPham = new SanPham(maSp, tenSp);
                return sanPham;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    
    public List<SanPham> selectAll() {
        List<SanPham> listSanPham = new ArrayList<>();
        String sql = "SELECT * FROM SANPHAM";
        try {
            Statement st = new DBContext().getConnect().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                int maSp = rs.getInt("MASANPHAM");
                String tenSp = rs.getString("TENSANPHAM");
                listSanPham.add(new SanPham(maSp, tenSp));
            }
        } catch (Exception e) {
            System.out.println("SANPHAM SERVICE ERROR SELECT ALL:" + e);
        }
        return listSanPham;
    }
    
    public List<SanPham> selectAllFromAToB(int a, int b) {
        List<SanPham> listSanPham = new ArrayList<>();
        String sql = String.format("SELECT * FROM SANPHAM ORDER BY SanPham.maSanPham OFFSET %s ROWS FETCH NEXT %s ROWS ONLY", a, b);
        try {
            Statement st = new DBContext().getConnect().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                int maSp = rs.getInt("MASANPHAM");
                String tenSp = rs.getString("TENSANPHAM");
                listSanPham.add(new SanPham(maSp, tenSp));
            }
        } catch (Exception e) {
            System.out.println("SANPHAM SERVICE ERROR SELECT ALL:" + e);
        }
        return listSanPham;
    }
    

    public String add(String tenSanPham) {
        Connection con = new DBContext().getConnect();
        String sql = "INSERT INTO SANPHAM (TENSANPHAM) VALUES (?)";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, tenSanPham);
            int result = st.executeUpdate();
            if (result > 0) {
                return "Thêm Thành Công";
            }
            return "Thêm Thất Bại";
        } catch (Exception e) {
            return "Thêm Lỗi: " + e;
        }
    }

    public String delete(int maSanPham) {
        Connection con = new DBContext().getConnect();
        String sql = "DELETE FROM SANPHAM WHERE MASANPHAM = ?";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, maSanPham);
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

    public String update(SanPham sanPham) {
        Connection con = new DBContext().getConnect();
        String sql = "UPDATE SANPHAM SET TENSANPHAM=? WHERE MASANPHAM = ?";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, sanPham.getTenSanPham());
            st.setInt(2, sanPham.getMaSanPham());

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