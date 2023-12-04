/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raven.service;

import com.raven.entity.VaiTro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.raven.database.DBContext;

/**
 *
 * @author asub
 */
public class VaitroService {

    public VaiTro findById(int maVaiTro) {
        String sql = "SELECT * FROM VAITRO WHERE MAVAITRO = " + maVaiTro;
//        System.out.println(sql);
        try {
            Statement statement = new DBContext().getConnect().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int mavaiTro = rs.getInt("MAVAITRO");
                String tenVaiTro = rs.getString("TENVAITRO");
                VaiTro Vaitro = new VaiTro(mavaiTro, tenVaiTro);
                return Vaitro;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<VaiTro> selectAll() {
        List<VaiTro> list = new ArrayList<>();
        String sql = "SELECT * FROM VAITRO";
        try {
            Statement st = new DBContext().getConnect().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                int mavaiTro = rs.getInt("MAVAITRO");
                String tenVaiTro = rs.getString("TENVAITRO");
                VaiTro Vaitro = new VaiTro(mavaiTro, tenVaiTro);

                list.add(new VaiTro(mavaiTro, tenVaiTro));
            }
        } catch (Exception e) {
            System.out.println("VAITRO SERVICE ERROR SELECT ALL:" + e);
        }
        return list;
    }

    public String add(String tenvaitro) {
        Connection con = new DBContext().getConnect();
        String sql = "INSERT INTO Vaitro (tenvaitro) VALUES (?)";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, tenvaitro);
            int result = st.executeUpdate();
            if (result > 0) {
                return "Thêm Thành Công";
            }
            return "Thêm Thất Bại";
        } catch (Exception e) {
            return "Thêm Lỗi: " + e;
        }
    }

    public String delete(int mavaitro) {
        Connection con = new DBContext().getConnect();
        String sql = "DELETE FROM VAITRO WHERE MAVAITRO = ?";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, mavaitro);
            int result = st.executeUpdate();
            if (result > 0) {
                return "Xóa Thành Công";
            }
            return "Xóa Thất Bại";
        } catch (Exception e) {
            if (e.toString().contains("FK")) {
                return "Xóa Lỗi: Tồn Tại Khóa Ngoại";
            }
            return "Xóa Lỗi: " + e;
        }
    }

    public String update(VaiTro vaitro) {
        Connection con = new DBContext().getConnect();
        String sql = "UPDATE VAITRO SET TENVAITRO=? WHERE MAVAITRO = ?";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(2, vaitro.getMaVaitro());
            st.setString(1, vaitro.getTenVaitro());

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
