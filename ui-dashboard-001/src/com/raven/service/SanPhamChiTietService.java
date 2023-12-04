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
import com.raven.entity.SanPhamChiTiet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SanPhamChiTietService {

    public List<SanPhamChiTiet> selectAllByCustomSql(String sql) {
        List<SanPhamChiTiet> listSpChiTiet = new ArrayList<>();
//        System.out.println("CUSTOM SQL :" + sql);
        try {
            Statement st = new DBContext().getConnect().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                int maSpChiTiet = rs.getInt("MASANPHAMCHITIET");
                int maSanPham = rs.getInt("MASANPHAM");
                int maHang = rs.getInt("MAHANG");
                int maMauSac = rs.getInt("MAMAUSAC");
                int maSize = rs.getInt("MASIZE");
                int maChatLieu = rs.getInt("MACHATLIEU");
                int soLuong = rs.getInt("SOLUONG");
                float giaSanPham = rs.getFloat("GIASANPHAM");
                String mota = rs.getString("MOTA");
                String hinhAnh = rs.getString("HINHANH");

                listSpChiTiet.add(new SanPhamChiTiet(maSpChiTiet, maSanPham, maHang, maMauSac, maSize, maChatLieu, soLuong, giaSanPham, mota, hinhAnh));
            }
        } catch (Exception e) {
            System.out.println("SANPHAMCHITIET SERVICE ERROR SELECT ALL:" + e);
        }
        return listSpChiTiet;
    }

    public int getTotalSanPhamChiTiet(int maSanPham) {
        String sql = String.format("SELECT SUM(soLuong) AS SOLUONG FROM SanPhamChiTiet WHERE maSanPham = %s", maSanPham);
        try {
            Statement statement = new DBContext().getConnect().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int soLuong = rs.getInt("SOLUONG");
                return soLuong;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;

    }

    public SanPhamChiTiet findById(int maSanPhamChiTiet) {
        String sql = "SELECT * FROM SANPHAMCHITIET WHERE MASANPHAMCHITIET = " + maSanPhamChiTiet;
        try {
            Statement statement = new DBContext().getConnect().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int maSpChiTiet = rs.getInt("MASANPHAMCHITIET");
                int maSanPham = rs.getInt("MASANPHAM");
                int maHang = rs.getInt("MAHANG");
                int maMauSac = rs.getInt("MAMAUSAC");
                int maSize = rs.getInt("MASIZE");
                int maChatLieu = rs.getInt("MACHATLIEU");
                int soLuong = rs.getInt("SOLUONG");
                float giaSanPham = rs.getFloat("GIASANPHAM");
                String mota = rs.getString("MOTA");
                String hinhAnh = rs.getString("HINHANH");

                SanPhamChiTiet spChiTiet = new SanPhamChiTiet(maSpChiTiet, maSanPham, maHang, maMauSac, maSize, maChatLieu, soLuong, giaSanPham, mota, hinhAnh);
                return spChiTiet;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<SanPhamChiTiet> selectAllFromAToB(int a, int b, int maSanPhamInput) {
        List<SanPhamChiTiet> listSpChiTiet = new ArrayList<>();
        String sql = String.format("SELECT * FROM SANPHAMCHITIET WHERE MASANPHAM = %s ORDER BY MASANPHAMCHITIET OFFSET %s ROWS FETCH NEXT %s ROWS ONLY ; ", maSanPhamInput, a, b);
        System.out.println(sql);
        try {
            Statement st = new DBContext().getConnect().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                int maSpChiTiet = rs.getInt("MASANPHAMCHITIET");
                int maSanPham = rs.getInt("MASANPHAM");
                int maHang = rs.getInt("MAHANG");
                int maMauSac = rs.getInt("MAMAUSAC");
                int maSize = rs.getInt("MASIZE");
                int maChatLieu = rs.getInt("MACHATLIEU");
                int soLuong = rs.getInt("SOLUONG");
                float giaSanPham = rs.getFloat("GIASANPHAM");
                String mota = rs.getString("MOTA");
                String hinhAnh = rs.getString("HINHANH");

                listSpChiTiet.add(new SanPhamChiTiet(maSpChiTiet, maSanPham, maHang, maMauSac, maSize, maChatLieu, soLuong, giaSanPham, mota, hinhAnh));
            }
        } catch (Exception e) {
            System.out.println("SANPHAMCHITIET SERVICE ERROR SELECT ALL:" + e);
        }
        return listSpChiTiet;
    }

    public List<SanPhamChiTiet> selectAll() {
        List<SanPhamChiTiet> listSpChiTiet = new ArrayList<>();
        String sql = "SELECT * FROM SANPHAMCHITIET";
        try {
            Statement st = new DBContext().getConnect().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                int maSpChiTiet = rs.getInt("MASANPHAMCHITIET");
                int maSanPham = rs.getInt("MASANPHAM");
                int maHang = rs.getInt("MAHANG");
                int maMauSac = rs.getInt("MAMAUSAC");
                int maSize = rs.getInt("MASIZE");
                int maChatLieu = rs.getInt("MACHATLIEU");
                int soLuong = rs.getInt("SOLUONG");
                float giaSanPham = rs.getFloat("GIASANPHAM");
                String mota = rs.getString("MOTA");
                String hinhAnh = rs.getString("HINHANH");

                listSpChiTiet.add(new SanPhamChiTiet(maSpChiTiet, maSanPham, maHang, maMauSac, maSize, maChatLieu, soLuong, giaSanPham, mota, hinhAnh));
            }
        } catch (Exception e) {
            System.out.println("SANPHAMCHITIET SERVICE ERROR SELECT ALL:" + e);
        }
        return listSpChiTiet;
    }
    public List<SanPhamChiTiet> selectAllByMaHoaDon(int maSanPhamInput) {
        List<SanPhamChiTiet> listSpChiTiet = new ArrayList<>();
        String sql = String.format("SELECT * FROM SANPHAMCHITIET WHERE MAHOADON = %s", maSanPhamInput);
        
        
        try {
            Statement st = new DBContext().getConnect().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                int maSpChiTiet = rs.getInt("MASANPHAMCHITIET");
                int maSanPham = rs.getInt("MASANPHAM");
                int maHang = rs.getInt("MAHANG");
                int maMauSac = rs.getInt("MAMAUSAC");
                int maSize = rs.getInt("MASIZE");
                int maChatLieu = rs.getInt("MACHATLIEU");
                int soLuong = rs.getInt("SOLUONG");
                float giaSanPham = rs.getFloat("GIASANPHAM");
                String mota = rs.getString("MOTA");
                String hinhAnh = rs.getString("HINHANH");
                listSpChiTiet.add(new SanPhamChiTiet(maSpChiTiet, maSanPham, maHang, maMauSac, maSize, maChatLieu, soLuong, giaSanPham, mota, hinhAnh));
            }
        } catch (Exception e) {
            System.out.println("SANPHAMCHITIET SERVICE ERROR SELECT ALL:" + e);
        }
        return listSpChiTiet;
    }

    public List<SanPhamChiTiet> selectAllByMaSanPham(int maSanPhamInput) {
        List<SanPhamChiTiet> listSpChiTiet = new ArrayList<>();
        String sql = String.format("SELECT * FROM SANPHAMCHITIET WHERE MASANPHAM = %s", maSanPhamInput);
        try {
            Statement st = new DBContext().getConnect().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                int maSpChiTiet = rs.getInt("MASANPHAMCHITIET");
                int maSanPham = rs.getInt("MASANPHAM");
                int maHang = rs.getInt("MAHANG");
                int maMauSac = rs.getInt("MAMAUSAC");
                int maSize = rs.getInt("MASIZE");
                int maChatLieu = rs.getInt("MACHATLIEU");
                int soLuong = rs.getInt("SOLUONG");
                float giaSanPham = rs.getFloat("GIASANPHAM");
                String mota = rs.getString("MOTA");
                String hinhAnh = rs.getString("HINHANH");
                listSpChiTiet.add(new SanPhamChiTiet(maSpChiTiet, maSanPham, maHang, maMauSac, maSize, maChatLieu, soLuong, giaSanPham, mota, hinhAnh));
            }
        } catch (Exception e) {
            System.out.println("SANPHAMCHITIET SERVICE ERROR SELECT ALL:" + e);
        }
        return listSpChiTiet;
    }

    public String add(SanPhamChiTiet spChiTiet) {
        Connection con = new DBContext().getConnect();
        String sql = "INSERT INTO SANPHAMCHITIET (MASANPHAM, MAHANG, MAMAUSAC, MASIZE, MACHATLIEU, SOLUONG, GIASANPHAM, MOTA, HINHANH) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, spChiTiet.getMaSanPham());
            st.setInt(2, spChiTiet.getMaHang());
            st.setInt(3, spChiTiet.getMaMauSac());
            st.setInt(4, spChiTiet.getMaSize());
            st.setInt(5, spChiTiet.getMaChatLieu());
            st.setInt(6, spChiTiet.getSoLuong());
            st.setFloat(7, spChiTiet.getGiaSanPham());
            st.setString(8, spChiTiet.getMota());
            st.setString(9, spChiTiet.getHinhAnh());

            int result = st.executeUpdate();
            if (result > 0) {
                return "Thêm Thành Công";
            }
            return "Thêm Thất Bại";
        } catch (Exception e) {
            return "Thêm Lỗi: " + e;
        }
    }

    public String delete(int maSanPhamChiTiet) {
        Connection con = new DBContext().getConnect();
        String sql = "DELETE FROM SANPHAMCHITIET WHERE MASANPHAMCHITIET = ?";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, maSanPhamChiTiet);
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

    public String update(SanPhamChiTiet spChiTiet) {
        Connection con = new DBContext().getConnect();
        String sql = "UPDATE SANPHAMCHITIET SET MASANPHAM=?, MAHANG=?, MAMAUSAC=?, MASIZE=?, MACHATLIEU=?, SOLUONG=?, GIASANPHAM=?, MOTA=?, HINHANH=? WHERE MASANPHAMCHITIET = ?";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, spChiTiet.getMaSanPham());
            st.setInt(2, spChiTiet.getMaHang());
            st.setInt(3, spChiTiet.getMaMauSac());
            st.setInt(4, spChiTiet.getMaSize());
            st.setInt(5, spChiTiet.getMaChatLieu());
            st.setInt(6, spChiTiet.getSoLuong());
            st.setFloat(7, spChiTiet.getGiaSanPham());
            st.setString(8, spChiTiet.getMota());
            st.setString(9, spChiTiet.getHinhAnh());
            st.setInt(10, spChiTiet.getMaSanPhamChiTiet());

            int result = st.executeUpdate();
            if (result > 0) {
                return "Update Thành Công";
            }
            return "Update Thất Bại";
        } catch (Exception e) {
            return "Update Lỗi: " + e;
        }
    }

    public String called(SanPhamChiTiet spChiTiet) {
        Connection con = new DBContext().getConnect();
        String sql = "UPDATE SANPHAMCHITIET SET MASANPHAM=?, MAHANG=?, MAMAUSAC=?, MASIZE=?, MACHATLIEU=?, SOLUONG=?, GIASANPHAM=?, MOTA=?, HINHANH=? WHERE MASANPHAMCHITIET = ?";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, spChiTiet.getMaSanPham());
            st.setInt(2, spChiTiet.getMaHang());
            st.setInt(3, spChiTiet.getMaMauSac());
            st.setInt(4, spChiTiet.getMaSize());
            st.setInt(5, spChiTiet.getMaChatLieu());
            st.setInt(6, spChiTiet.getSoLuong());
            st.setFloat(7, spChiTiet.getGiaSanPham());
            st.setString(8, spChiTiet.getMota());
            st.setString(9, spChiTiet.getHinhAnh());
            st.setInt(10, spChiTiet.getMaSanPhamChiTiet());

            int result = st.executeUpdate();
            if (result > 0) {
                return "Hủy Thành Công";
            }
            return "Hủy Thất Bại";
        } catch (Exception e) {
            return "Hủy Lỗi: " + e;
        }
    }
}
