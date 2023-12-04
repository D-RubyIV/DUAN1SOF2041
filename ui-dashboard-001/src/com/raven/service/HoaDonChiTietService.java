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
import com.raven.entity.HoaDonChiTiet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HoaDonChiTietService {

    public HoaDonChiTiet findById(int maHoaDonChiTiet) {
        String sql = "SELECT * FROM HOADONCHITIET WHERE MAHOADONCHITIET = " + maHoaDonChiTiet;
        try {
            Statement statement = new DBContext().getConnect().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int maHdct = rs.getInt("MAHOADONCHITIET");
                int maSpChiTiet = rs.getInt("MASANPHAMCHITIET");
                int maHd = rs.getInt("MAHOADON");
                int soLuong = rs.getInt("SOLUONG");

                HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet(maHdct, maSpChiTiet, maHd, soLuong);
                return hoaDonChiTiet;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    
    public HoaDonChiTiet findBy_MaHoaDon_MaSPCT(int maHoaDon, int maSPCT) {
        String sql = String.format("SELECT * FROM HOADONCHITIET WHERE MAHOADON = %s AND MASANPHAMCHITIET = %s", maHoaDon, maSPCT);
        try {
            Statement statement = new DBContext().getConnect().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int maHdct = rs.getInt("MAHOADONCHITIET");
                int maSpChiTiet = rs.getInt("MASANPHAMCHITIET");
                int maHd = rs.getInt("MAHOADON");
                int soLuong = rs.getInt("SOLUONG");

                HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet(maHdct, maSpChiTiet, maHd, soLuong);
                return hoaDonChiTiet;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    
    

    public List<HoaDonChiTiet> selectAll() {
        List<HoaDonChiTiet> listHoaDonChiTiet = new ArrayList<>();
        String sql = "SELECT * FROM HOADONCHITIET";
        try {
            Statement st = new DBContext().getConnect().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                int maHdct = rs.getInt("MAHOADONCHITIET");
                int maSpChiTiet = rs.getInt("MASANPHAMCHITIET");
                int maHd = rs.getInt("MAHOADON");
                int soLuong = rs.getInt("SOLUONG");

                listHoaDonChiTiet.add(new HoaDonChiTiet(maHdct, maSpChiTiet, maHd, soLuong));
            }
        } catch (Exception e) {
            System.out.println("HOADONCHITIET SERVICE ERROR SELECT ALL:" + e);
        }
        return listHoaDonChiTiet;
    }
    
     public List<HoaDonChiTiet> selectAllByMaHoaDon(int maHoaDon) {
        List<HoaDonChiTiet> listHoaDonChiTiet = new ArrayList<>();
        String sql = String.format("SELECT * FROM HOADONCHITIET WHERE MAHOADON = %s", maHoaDon);
        try {
            Statement st = new DBContext().getConnect().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                int maHdct = rs.getInt("MAHOADONCHITIET");
                int maSpChiTiet = rs.getInt("MASANPHAMCHITIET");
                int maHd = rs.getInt("MAHOADON");
                int soLuong = rs.getInt("SOLUONG");
                listHoaDonChiTiet.add(new HoaDonChiTiet(maHdct, maSpChiTiet, maHd, soLuong));
            }
        } catch (Exception e) {
            System.out.println("HOADONCHITIET SERVICE ERROR SELECT ALL:" + e);
        }
        return listHoaDonChiTiet;
    }

    public String add(HoaDonChiTiet hoaDonChiTiet) {
        Connection con = new DBContext().getConnect();
        String sql = "INSERT INTO HOADONCHITIET (MASANPHAMCHITIET, MAHOADON, SOLUONG) VALUES (?, ?, ?)";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, hoaDonChiTiet.getMaSanPhamChiTiet());
            st.setInt(2, hoaDonChiTiet.getMaHoaDon());
            st.setInt(3, hoaDonChiTiet.getSoLuong());

            int result = st.executeUpdate();
            if (result > 0) {
                return "Thêm Thành Công";
            }
            return "Thêm Thất Bại";
        } catch (Exception e) {
            return "Thêm Lỗi: " + e;
        }
    }

    public String delete(int maHoaDonChiTiet) {
        Connection con = new DBContext().getConnect();
        String sql = "DELETE FROM HOADONCHITIET WHERE MAHOADONCHITIET = ?";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, maHoaDonChiTiet);
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

    public String update(HoaDonChiTiet hoaDonChiTiet) {
        Connection con = new DBContext().getConnect();
        String sql = "UPDATE HOADONCHITIET SET MASANPHAMCHITIET=?, MAHOADON=?, SOLUONG=? WHERE MAHOADONCHITIET = ?";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, hoaDonChiTiet.getMaSanPhamChiTiet());
            st.setInt(2, hoaDonChiTiet.getMaHoaDon());
            st.setInt(3, hoaDonChiTiet.getSoLuong());
            st.setInt(4, hoaDonChiTiet.getMaHoaDonChiTiet());

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