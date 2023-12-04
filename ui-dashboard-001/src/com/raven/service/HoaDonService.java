/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raven.service;

import java.sql.Connection;
import com.raven.database.DBContext;
import com.raven.entity.HoaDon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author phamh
 */
public class HoaDonService {

    public HoaDon findById(int maHoaDon) {
        String sql = "SELECT * FROM HOADON WHERE MAHOADON = " + maHoaDon;
        try {
            Statement statement = new DBContext().getConnect().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int maHd = rs.getInt("MAHOADON");
                int maNguoiBan = rs.getInt("MANGUOIBAN");
                int maKhachHang = rs.getInt("MAKHACHHANG");
                String maGiamGia = rs.getString("MAGIAMGIA");
                Date thoiGianTao = rs.getDate("THOIGIANTAO");
                Date thoiGianThanhToan = rs.getDate("THOIGIANTHANHTOAN");
                float tongTienTruocGiamGia = rs.getFloat("TONGTIENTRUOCGIAMGIA");
                float tongTienSauGiamGia = rs.getFloat("TONGTIENSAUGIAMGIA");
                float tongTienGiamGia = rs.getFloat("TONGTIENGIAMGIA");
                int trangThaiThanhToan = rs.getInt("TRANGTHAITHANHTOAN");

                HoaDon hoaDon = new HoaDon(maHd, maNguoiBan, maKhachHang, maGiamGia, thoiGianTao,
                        thoiGianThanhToan, tongTienTruocGiamGia, tongTienSauGiamGia, tongTienGiamGia,
                        trangThaiThanhToan);
                return hoaDon;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<HoaDon> selectAllByMaTrangThaiThanhToan(int inputTrangThaiThanhToan) {
        List<HoaDon> listHoaDon = new ArrayList<>();
        String sql = String.format("SELECT * FROM HOADON WHERE TRANGTHAITHANHTOAN = %s", inputTrangThaiThanhToan);
        try {
            Statement st = new DBContext().getConnect().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                int maHd = rs.getInt("MAHOADON");
                int maNguoiBan = rs.getInt("MANGUOIBAN");
                int maKhachHang = rs.getInt("MAKHACHHANG");
                String maGiamGia = rs.getString("MAGIAMGIA");
                Date thoiGianTao = rs.getDate("THOIGIANTAO");
                Date thoiGianThanhToan = rs.getDate("THOIGIANTHANHTOAN");
                float tongTienTruocGiamGia = rs.getFloat("TONGTIENTRUOCGIAMGIA");
                float tongTienSauGiamGia = rs.getFloat("TONGTIENSAUGIAMGIA");
                float tongTienGiamGia = rs.getFloat("TONGTIENGIAMGIA");
                int trangThaiThanhToan = rs.getInt("TRANGTHAITHANHTOAN");

                listHoaDon.add(new HoaDon(maHd, maNguoiBan, maKhachHang, maGiamGia, thoiGianTao,
                        thoiGianThanhToan, tongTienTruocGiamGia, tongTienSauGiamGia, tongTienGiamGia,
                        trangThaiThanhToan));
            }
        } catch (Exception e) {
            System.out.println("HOADON SERVICE ERROR SELECT ALL:" + e);
        }
        return listHoaDon;
    }

    public List<HoaDon> selectAll() {
        List<HoaDon> listHoaDon = new ArrayList<>();
        String sql = "SELECT * FROM HOADON";
        try {
            Statement st = new DBContext().getConnect().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                int maHd = rs.getInt("MAHOADON");
                int maNguoiBan = rs.getInt("MANGUOIBAN");
                int maKhachHang = rs.getInt("MAKHACHHANG");
                String maGiamGia = rs.getString("MAGIAMGIA");
                Date thoiGianTao = rs.getDate("THOIGIANTAO");
                Date thoiGianThanhToan = rs.getDate("THOIGIANTHANHTOAN");
                float tongTienTruocGiamGia = rs.getFloat("TONGTIENTRUOCGIAMGIA");
                float tongTienSauGiamGia = rs.getFloat("TONGTIENSAUGIAMGIA");
                float tongTienGiamGia = rs.getFloat("TONGTIENGIAMGIA");
                int trangThaiThanhToan = rs.getInt("TRANGTHAITHANHTOAN");

                listHoaDon.add(new HoaDon(maHd, maNguoiBan, maKhachHang, maGiamGia, thoiGianTao,
                        thoiGianThanhToan, tongTienTruocGiamGia, tongTienSauGiamGia, tongTienGiamGia,
                        trangThaiThanhToan));
            }
        } catch (Exception e) {
            System.out.println("HOADON SERVICE ERROR SELECT ALL:" + e);
        }
        return listHoaDon;
    }
    
    public List<HoaDon> selectAllByCustomSql(String sql) {
        List<HoaDon> listHoaDon = new ArrayList<>();
        try {
            Statement st = new DBContext().getConnect().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                int maHd = rs.getInt("MAHOADON");
                int maNguoiBan = rs.getInt("MANGUOIBAN");
                int maKhachHang = rs.getInt("MAKHACHHANG");
                String maGiamGia = rs.getString("MAGIAMGIA");
                Date thoiGianTao = rs.getDate("THOIGIANTAO");
                Date thoiGianThanhToan = rs.getDate("THOIGIANTHANHTOAN");
                float tongTienTruocGiamGia = rs.getFloat("TONGTIENTRUOCGIAMGIA");
                float tongTienSauGiamGia = rs.getFloat("TONGTIENSAUGIAMGIA");
                float tongTienGiamGia = rs.getFloat("TONGTIENGIAMGIA");
                int trangThaiThanhToan = rs.getInt("TRANGTHAITHANHTOAN");

                listHoaDon.add(new HoaDon(maHd, maNguoiBan, maKhachHang, maGiamGia, thoiGianTao,
                        thoiGianThanhToan, tongTienTruocGiamGia, tongTienSauGiamGia, tongTienGiamGia,
                        trangThaiThanhToan));
            }
        } catch (Exception e) {
            System.out.println("HOADON SERVICE ERROR SELECT ALL:" + e);
        }
        return listHoaDon;
    }



    public String add(HoaDon hoaDon) {
        Connection con = new DBContext().getConnect();
        String sql = "INSERT INTO HOADON (MANGUOIBAN, MAKHACHHANG, MAGIAMGIA, THOIGIANTAO, "
                + "THOIGIANTHANHTOAN, TONGTIENTRUOCGIAMGIA, TONGTIENSAUGIAMGIA, TONGTIENGIAMGIA, "
                + "TRANGTHAITHANHTOAN) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, hoaDon.getMaNguoiBan());
            st.setInt(2, hoaDon.getMaKhachHang());
            st.setString(3, hoaDon.getMaGiamGia());
            st.setDate(4, new java.sql.Date(hoaDon.getThoiGianTao().getTime()));
            st.setDate(5, new java.sql.Date(hoaDon.getThoiGianThanhToan().getTime()));
            st.setFloat(6, hoaDon.getTongTienTruocGiamGia());
            st.setFloat(7, hoaDon.getTongTienSauGiamGia());
            st.setFloat(8, hoaDon.getTongTienGiamGia());
            st.setInt(9, hoaDon.getTrangThaiThanhToan());
            System.out.println("da tem");

            int result = st.executeUpdate();
            if (result > 0) {
                return "Thêm Thành Công";
            }
            return "Thêm Thất Bại";
        } catch (Exception e) {
            return "Thêm Lỗi: " + e;
        }
    }

    public String delete(int maHoaDon) {
        Connection con = new DBContext().getConnect();
        String sql = "DELETE FROM HOADON WHERE MAHOADON = ?";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, maHoaDon);
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

    public String update(HoaDon hoaDon) {
        Connection con = new DBContext().getConnect();
        String sql = "UPDATE HOADON SET MANGUOIBAN=?, MAKHACHHANG=?, MAGIAMGIA=?, "
                + "THOIGIANTAO=?, THOIGIANTHANHTOAN=?, TONGTIENTRUOCGIAMGIA=?, TONGTIENSAUGIAMGIA=?, "
                + "TONGTIENGIAMGIA=?, TRANGTHAITHANHTOAN=? WHERE MAHOADON = ?";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, hoaDon.getMaNguoiBan());
            st.setInt(2, hoaDon.getMaKhachHang());
            st.setString(3, hoaDon.getMaGiamGia());
            st.setDate(4, new java.sql.Date(hoaDon.getThoiGianTao().getTime()));
            st.setDate(5, new java.sql.Date(hoaDon.getThoiGianThanhToan().getTime()));
            st.setFloat(6, hoaDon.getTongTienTruocGiamGia());
            st.setFloat(7, hoaDon.getTongTienSauGiamGia());
            st.setFloat(8, hoaDon.getTongTienGiamGia());
            st.setInt(9, hoaDon.getTrangThaiThanhToan());
            st.setInt(10, hoaDon.getMaHoaDon());

            int result = st.executeUpdate();
            if (result > 0) {
                return "Update Thành Công";
            }
            return "Update Thất Bại";
        } catch (Exception e) {
            return "Update Lỗi: " + e;
        }
    }

    public String updateMaKhuyeMai(HoaDon hoaDon) {
        Connection con = new DBContext().getConnect();
        String sql = "UPDATE HOADON SET MANGUOIBAN=?, MAKHACHHANG=?, MAGIAMGIA=?, "
                + "THOIGIANTAO=?, THOIGIANTHANHTOAN=?, TONGTIENTRUOCGIAMGIA=?, TONGTIENSAUGIAMGIA=?, "
                + "TONGTIENGIAMGIA=?, TRANGTHAITHANHTOAN=? WHERE MAHOADON = ?";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, hoaDon.getMaNguoiBan());
            st.setInt(2, hoaDon.getMaKhachHang());
            st.setString(3, hoaDon.getMaGiamGia());
            st.setDate(4, new java.sql.Date(hoaDon.getThoiGianTao().getTime()));
            st.setDate(5, new java.sql.Date(hoaDon.getThoiGianThanhToan().getTime()));
            st.setFloat(6, hoaDon.getTongTienTruocGiamGia());
            st.setFloat(7, hoaDon.getTongTienSauGiamGia());
            st.setFloat(8, hoaDon.getTongTienGiamGia());
            st.setInt(9, hoaDon.getTrangThaiThanhToan());
            st.setInt(10, hoaDon.getMaHoaDon());

            int result = st.executeUpdate();
            if (result > 0) {
                return "Áp Mã Khuyến Mãi Thành Công";
            }
            return "Áp Mã Khuyến Mãi  Thất Bại";
        } catch (Exception e) {
            return "Áp Mã Khuyến Mãi  Lỗi: " + e;
        }
    }

    public String complete(HoaDon hoaDon) {
        Connection con = new DBContext().getConnect();
        String sql = "UPDATE HOADON SET MANGUOIBAN=?, MAKHACHHANG=?, MAGIAMGIA=?, "
                + "THOIGIANTAO=?, THOIGIANTHANHTOAN=?, TONGTIENTRUOCGIAMGIA=?, TONGTIENSAUGIAMGIA=?, "
                + "TONGTIENGIAMGIA=?, TRANGTHAITHANHTOAN=? WHERE MAHOADON = ?";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, hoaDon.getMaNguoiBan());
            st.setInt(2, hoaDon.getMaKhachHang());
            st.setString(3, hoaDon.getMaGiamGia());
            st.setDate(4, new java.sql.Date(hoaDon.getThoiGianTao().getTime()));
            st.setDate(5, new java.sql.Date(hoaDon.getThoiGianThanhToan().getTime()));
            st.setFloat(6, hoaDon.getTongTienTruocGiamGia());
            st.setFloat(7, hoaDon.getTongTienSauGiamGia());
            st.setFloat(8, hoaDon.getTongTienGiamGia());
            st.setInt(9, hoaDon.getTrangThaiThanhToan());
            st.setInt(10, hoaDon.getMaHoaDon());

            int result = st.executeUpdate();
            if (result > 0) {
                return "Thanh toán thành Công";
            }
            return "Thanh toán Thất Bại";
        } catch (Exception e) {
            return "Thanh toán Lỗi: " + e;
        }
    }

    public String called(HoaDon hoaDon) {
        Connection con = new DBContext().getConnect();
        String sql = "UPDATE HOADON SET MANGUOIBAN=?, MAKHACHHANG=?, MAGIAMGIA=?, "
                + "THOIGIANTAO=?, THOIGIANTHANHTOAN=?, TONGTIENTRUOCGIAMGIA=?, TONGTIENSAUGIAMGIA=?, "
                + "TONGTIENGIAMGIA=?, TRANGTHAITHANHTOAN=? WHERE MAHOADON = ?";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, hoaDon.getMaNguoiBan());
            st.setInt(2, hoaDon.getMaKhachHang());
            st.setString(3, hoaDon.getMaGiamGia());
            st.setDate(4, new java.sql.Date(hoaDon.getThoiGianTao().getTime()));
            st.setDate(5, new java.sql.Date(hoaDon.getThoiGianThanhToan().getTime()));
            st.setFloat(6, hoaDon.getTongTienTruocGiamGia());
            st.setFloat(7, hoaDon.getTongTienSauGiamGia());
            st.setFloat(8, hoaDon.getTongTienGiamGia());
            st.setInt(9, hoaDon.getTrangThaiThanhToan());
            st.setInt(10, hoaDon.getMaHoaDon());

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
