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
import com.ravent.entity.KhuyenMai;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KhuyenMaiService {

    public KhuyenMai findById(String maKhuyenMai) {
        String sql = "SELECT * FROM KHUYENMAI WHERE MAKHUYENMAI LIKE '" + maKhuyenMai + "'";
//        System.out.println(sql);
        try {
            Statement statement = new DBContext().getConnect().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                String maKm = rs.getString("MAKHUYENMAI");
                String tenKm = rs.getString("TENKHUYENMAI");
                float menhGia = rs.getFloat("MENHGIA");
                int soLuong = rs.getInt("SOLUONGMA");
                Date ngayTao = rs.getDate("NGAYTAO");
                Date ngayBatDau = rs.getDate("NGAYBATDAU");
                Date ngayKetThuc = rs.getDate("NGAYKETTHUC");
                KhuyenMai khuyenMai = new KhuyenMai(maKm, tenKm, menhGia, soLuong, ngayTao, ngayBatDau, ngayKetThuc);
                return khuyenMai;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<KhuyenMai> selectAll() {
        List<KhuyenMai> listKhuyenMai = new ArrayList<>();
        String sql = "SELECT * FROM KHUYENMAI";
        try {
            Statement st = new DBContext().getConnect().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String maKm = rs.getString("MAKHUYENMAI");
                String tenKm = rs.getString("TENKHUYENMAI");
                float menhGia = rs.getFloat("MENHGIA");
                int soLuong = rs.getInt("SOLUONGMA");
                Date ngayTao = rs.getDate("NGAYTAO");
                Date ngayBatDau = rs.getDate("NGAYBATDAU");
                Date ngayKetThuc = rs.getDate("NGAYKETTHUC");
                listKhuyenMai.add(new KhuyenMai(maKm, tenKm, menhGia, soLuong, ngayTao, ngayBatDau, ngayKetThuc));
            }
        } catch (Exception e) {
            System.out.println("KHUYENMAI SERVICE ERROR SELECT ALL:" + e);
        }
        return listKhuyenMai;
    }

    public String add(KhuyenMai khuyenMai) {
        Connection con = new DBContext().getConnect();
        String sql = "INSERT KHUYENMAI (MAKHUYENMAI, TENKHUYENMAI, MENHGIA, SOLUONGMA, NGAYTAO, NGAYBATDAU, NGAYKETTHUC) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, khuyenMai.getMaKhuyenMai());
            st.setString(2, khuyenMai.getTenKhuyenMai());
            st.setFloat(3, khuyenMai.getMenhGia());
            st.setInt(4, khuyenMai.getSoLuong());
            st.setDate(5, new java.sql.Date(khuyenMai.getNgayTao().getTime()));
            st.setDate(6, new java.sql.Date(khuyenMai.getNgayBatDau().getTime()));
            st.setDate(7, new java.sql.Date(khuyenMai.getNgayKetThuc().getTime()));
            int result = st.executeUpdate();
            if (result > 0) {
                return "Thêm Thành Công";
            }
            return "Thêm Thất Bại";
        } catch (Exception e) {
            return "Thêm Lỗi: " + e;
        }
    }

    public String delete(String maKhuyenMai) {
        Connection con = new DBContext().getConnect();
        String sql = "DELETE FROM KHUYENMAI WHERE MAKHUYENMAI = ?";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, maKhuyenMai);
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

    public String update(KhuyenMai khuyenMai) {
        Connection con = new DBContext().getConnect();
        String sql = "UPDATE KHUYENMAI SET TENKHUYENMAI=?, MENHGIA=?, SOLUONGMA=?, NGAYTAO=?, NGAYBATDAU=?, NGAYKETTHUC=? WHERE MAKHUYENMAI LIKE ?";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, khuyenMai.getTenKhuyenMai());
            st.setFloat(2, khuyenMai.getMenhGia());
            st.setInt(3, khuyenMai.getSoLuong());
            st.setDate(4, new java.sql.Date(khuyenMai.getNgayTao().getTime()));
            st.setDate(5, new java.sql.Date(khuyenMai.getNgayBatDau().getTime()));
            st.setDate(6, new java.sql.Date(khuyenMai.getNgayKetThuc().getTime()));
            st.setString(7, khuyenMai.getMaKhuyenMai());

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