/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raven.service;

import com.raven.database.DBContext;
import com.raven.entity.NguoiDung;
import com.sun.org.apache.bcel.internal.generic.AALOAD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author asub
 */
public class NguoiDungService {

    public NguoiDung findById(int maNguoiDung) {
        String sql = "SELECT * FROM NGUOIDUNG WHERE MANGUOIDUNG = " + maNguoiDung;
        try {
            Statement statement = new DBContext().getConnect().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int maNd = rs.getInt("MANGUOIDUNG");
                int maVt = rs.getInt("MAVAITRO");
                String tenNd = rs.getString("TENNGUOIDUNG");
                String soDt = rs.getString("SODIENTHOAI");
                String email = rs.getString("EMAIL");
                String tenTk = rs.getString("TENTAIKHOAN");
                String matKhau = rs.getString("MATKHAU");

                NguoiDung nguoiDung = new NguoiDung(maNd, maVt, tenNd, soDt, email, tenTk, matKhau);
                return nguoiDung;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<NguoiDung> selectAll() {
        List<NguoiDung> listNguoiDung = new ArrayList<>();
        String sql = "SELECT * FROM NGUOIDUNG";
        try {
            Statement st = new DBContext().getConnect().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                int maNd = rs.getInt("MANGUOIDUNG");
                int maVt = rs.getInt("MAVAITRO");
                String tenNd = rs.getString("TENNGUOIDUNG");
                String soDt = rs.getString("SODIENTHOAI");
                String email = rs.getString("EMAIL");
                String tenTk = rs.getString("TENTAIKHOAN");
                String matKhau = rs.getString("MATKHAU");

                listNguoiDung.add(new NguoiDung(maNd, maVt, tenNd, soDt, email, tenTk, matKhau));
            }
        } catch (Exception e) {
            System.out.println("NGUOIDUNG SERVICE ERROR SELECT ALL:" + e);
        }
        return listNguoiDung;
    }

    public List<NguoiDung> selectAllByCustomSql(String sql) {
        List<NguoiDung> listNguoiDung = new ArrayList<>();
        try {
            Statement st = new DBContext().getConnect().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                int maNd = rs.getInt("MANGUOIDUNG");
                int maVt = rs.getInt("MAVAITRO");
                String tenNd = rs.getString("TENNGUOIDUNG");
                String soDt = rs.getString("SODIENTHOAI");
                String email = rs.getString("EMAIL");
                String tenTk = rs.getString("TENTAIKHOAN");
                String matKhau = rs.getString("MATKHAU");

                listNguoiDung.add(new NguoiDung(maNd, maVt, tenNd, soDt, email, tenTk, matKhau));
            }
        } catch (Exception e) {
            System.out.println("NGUOIDUNG SERVICE ERROR SELECT ALL:" + e);
        }
        return listNguoiDung;
    }

    public List<NguoiDung> selectAllByIdVaiTro(int maVaiTro) {
        List<NguoiDung> listNguoiDung = new ArrayList<>();
        String sql = String.format("SELECT * FROM NGUOIDUNG WHERE MAVAITRO = %s", maVaiTro);
        try {
            Statement st = new DBContext().getConnect().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                int maNd = rs.getInt("MANGUOIDUNG");
                int maVt = rs.getInt("MAVAITRO");
                String tenNd = rs.getString("TENNGUOIDUNG");
                String soDt = rs.getString("SODIENTHOAI");
                String email = rs.getString("EMAIL");
                String tenTk = rs.getString("TENTAIKHOAN");
                String matKhau = rs.getString("MATKHAU");

                listNguoiDung.add(new NguoiDung(maNd, maVt, tenNd, soDt, email, tenTk, matKhau));
            }
        } catch (Exception e) {
            System.out.println("NGUOIDUNG SERVICE ERROR SELECT ALL:" + e);
        }
        return listNguoiDung;
    }

    public String add(NguoiDung nguoiDung) {
        Connection con = new DBContext().getConnect();
        String sql = "INSERT INTO NGUOIDUNG (MAVAITRO, TENNGUOIDUNG, SODIENTHOAI, EMAIL, TENTAIKHOAN, MATKHAU) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, nguoiDung.getMaVaiTro());
            st.setString(2, nguoiDung.getTenNguoiDung());
            st.setString(3, nguoiDung.getSoDienThoai());
            st.setString(4, nguoiDung.getEmail());
            st.setString(5, nguoiDung.getTenTaiKhoan());
            st.setString(6, nguoiDung.getMatKhau());

            int result = st.executeUpdate();
            if (result > 0) {
                return "Thêm Thành Công";
            }
            return "Thêm Thất Bại";
        } catch (Exception e) {
            return "Thêm Lỗi: " + e;
        }
    }

    public String delete(int maNguoiDung) {
        Connection con = new DBContext().getConnect();
        String sql = "DELETE FROM NGUOIDUNG WHERE MANGUOIDUNG = ?";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, maNguoiDung);
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

    public String update(NguoiDung nguoiDung) {
        Connection con = new DBContext().getConnect();
        String sql = "UPDATE NGUOIDUNG SET MAVAITRO=?, TENNGUOIDUNG=?, SODIENTHOAI=?, EMAIL=?, TENTAIKHOAN=?, MATKHAU=? WHERE MANGUOIDUNG = ?";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, nguoiDung.getMaVaiTro());
            st.setString(2, nguoiDung.getTenNguoiDung());
            st.setString(3, nguoiDung.getSoDienThoai());
            st.setString(4, nguoiDung.getEmail());
            st.setString(5, nguoiDung.getTenTaiKhoan());
            st.setString(6, nguoiDung.getMatKhau());
            st.setInt(7, nguoiDung.getMaNguoiDung());

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
