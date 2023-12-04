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
import com.raven.entity.Hang;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class HangService {

    public Hang findById(int maHang) {
        String sql = "SELECT * FROM HANG WHERE MAHANG = " + maHang;
//        System.out.println(sql);
        try {
            Statement statement = new DBContext().getConnect().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int maHangResult = rs.getInt("MAHANG");
                String tenHang = rs.getString("TENHANG");
                Hang hang = new Hang(maHangResult, tenHang);
                return hang;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<Hang> selectAll() {
        List<Hang> listHang = new ArrayList<>();
        String sql = "SELECT * FROM HANG";
        try {
            Statement st = new DBContext().getConnect().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                int maHang = rs.getInt("MAHANG");
                String tenHang = rs.getString("TENHANG");
                listHang.add(new Hang(maHang, tenHang));
            }
        } catch (Exception e) {
            System.out.println("HANG SERVICE ERROR SELECT ALL:" + e);
        }
        return listHang;
    }

    public String add(String tenHang) {
        Connection con = new DBContext().getConnect();
        String sql = "INSERT INTO HANG (TENHANG) VALUES (?)";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, tenHang);
            int result = st.executeUpdate();
            if (result > 0) {
                return "Thêm Thành Công";
            }
            return "Thêm Thất Bại";
        } catch (Exception e) {
            return "Thêm Lỗi: " + e;
        }
    }

    public String delete(int maHang) {
        Connection con = new DBContext().getConnect();
        String sql = "DELETE FROM HANG WHERE MAHANG = ?";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, maHang);
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

    public String update(Hang hang) {
        Connection con = new DBContext().getConnect();
        String sql = "UPDATE HANG SET TENHANG=? WHERE MAHANG = ?";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, hang.getTenHang());
            st.setInt(2, hang.getMaHang());
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