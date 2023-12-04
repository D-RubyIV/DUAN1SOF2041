/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raven.service;

/**
 *
 * @author phamh
 */

import com.raven.database.DBContext;
import com.raven.entity.Size;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class SizeService {

    public Size findById(int maSize) {
        String sql = "SELECT * FROM SIZE WHERE MASIZE = " + maSize;
//        System.out.println(sql);
        try {
            Statement statement = new DBContext().getConnect().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int maSizeResult = rs.getInt("MASIZE");
                String tenSize = rs.getString("TENSIZE");
                Size size = new Size(maSizeResult, tenSize);
                return size;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<Size> selectAll() {
        List<Size> listSize = new ArrayList<>();
        String sql = "SELECT * FROM SIZE";
        try {
            Statement st = new DBContext().getConnect().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                int maSize = rs.getInt("MASIZE");
                String tenSize = rs.getString("TENSIZE");
                listSize.add(new Size(maSize, tenSize));
            }
        } catch (Exception e) {
            System.out.println("SIZE SERVICE ERROR SELECT ALL:" + e);
        }
        return listSize;
    }

    public String add(String tenSize) {
        Connection con = new DBContext().getConnect();
        String sql = "INSERT INTO SIZE (TENSIZE) VALUES (?)";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, tenSize);
            int result = st.executeUpdate();
            if (result > 0) {
                return "Thêm Thành Công";
            }
            return "Thêm Thất Bại";
        } catch (Exception e) {
            return "Thêm Lỗi: " + e;
        }
    }

    public String delete(int maSize) {
        Connection con = new DBContext().getConnect();
        String sql = "DELETE FROM SIZE WHERE MASIZE = ?";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, maSize);
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

    public String update(Size size) {
        Connection con = new DBContext().getConnect();
        String sql = "UPDATE SIZE SET TENSIZE=? WHERE MASIZE = ?";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, size.getTenSize());
            st.setInt(2, size.getMaSize());
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