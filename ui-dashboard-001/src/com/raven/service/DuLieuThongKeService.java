/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raven.service;

import com.raven.database.DBContext;
import com.raven.entity.DuLieuThongKeDoanhThu;
import com.raven.entity.DuLieuThongKeHoaDon;
import com.raven.entity.Hang;
import com.raven.entity.SanPhamChiTiet;
import java.util.List;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author phamh
 */
public class DuLieuThongKeService {

    public List<DuLieuThongKeDoanhThu> selectAllDoanhThuByDate(Date date1, Date date2) {
        // Tạo đối tượng SimpleDateFormat với định dạng mong muốn
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Chuyển đổi Date thành chuỗi với định dạng
        String formattedDate = dateFormat.format(date1);
        String sql = String.format("SELECT thoiGianThanhToan AS Ngay,SUM(tongTienSauGiamGia) AS DoanhThu FROM HoaDon WHERE thoiGianThanhToan BETWEEN '%s' AND '%s' GROUP BY thoiGianThanhToan", dateFormat.format(date1), dateFormat.format(date2));
        System.out.println(sql);
        System.out.println(date1.toString());
        System.out.println(date2.toString());
        List<DuLieuThongKeDoanhThu> listDuLieuThongKe = new ArrayList<>();
        List<Date> listDate = new ArrayList<>();

        try {
            Statement statement = new DBContext().getConnect().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                Date date = rs.getDate(1);
                int value = rs.getInt(2);
                DuLieuThongKeDoanhThu duLieuThongKe = new DuLieuThongKeDoanhThu(date, value);
                listDuLieuThongKe.add(duLieuThongKe);
                listDate.add(date);
            }
            LocalDate startDate = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate endDate = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            for (LocalDate localDate = startDate; !localDate.isAfter(endDate); localDate = localDate.plusDays(1)) {
                Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

//                System.out.println(date.toString());
                if (!listDate.contains(date)) {
                    listDuLieuThongKe.add(new DuLieuThongKeDoanhThu(date, 0));
                }
            }
        } catch (Exception e) {
            System.out.println("Select Du Lieu Error: " + e);
        }

        return listDuLieuThongKe;
    }

    public List<DuLieuThongKeHoaDon> selectAllHoaDonByDate(Date date1, Date date2) {
        // Tạo đối tượng SimpleDateFormat với định dạng mong muốn
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Chuyển đổi Date thành chuỗi với định dạng
        String formattedDate = dateFormat.format(date1);
        String sql = String.format("SELECT thoiGianThanhToan AS Ngay, COUNT(CASE WHEN [trangThaiThanhToan] = '2' THEN 1 END) AS DATHANHTOAN, COUNT(CASE WHEN [trangThaiThanhToan] = '3' THEN 1 END) AS HUYTHANHTOAN FROM HoaDon WHERE thoiGianThanhToan BETWEEN '%s' AND '%s' GROUP BY thoiGianThanhToan;", dateFormat.format(date1), dateFormat.format(date2));
        System.out.println(sql);
        System.out.println(date1.toString());
        System.out.println(date2.toString());
        List<DuLieuThongKeHoaDon> lieuThongKeHoaDons = new ArrayList<>();
        List<Date> listDate = new ArrayList<>();

        try {
            Statement statement = new DBContext().getConnect().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                Date date = rs.getDate(1);
                int daThanhToan = rs.getInt(2);
                int huyThanhToan = rs.getInt(3);
                DuLieuThongKeHoaDon duLieuThongKe = new DuLieuThongKeHoaDon(date, daThanhToan, huyThanhToan);
                lieuThongKeHoaDons.add(duLieuThongKe);
                listDate.add(date);
            }
            LocalDate startDate = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate endDate = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            for (LocalDate localDate = startDate; !localDate.isAfter(endDate); localDate = localDate.plusDays(1)) {
                Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

//                System.out.println(date.toString());
                if (!listDate.contains(date)) {
                    lieuThongKeHoaDons.add(new DuLieuThongKeHoaDon(date, 0, 0));
                }
            }
        } catch (Exception e) {
            System.out.println("Select Du Lieu Error: " + e);
        }

        return lieuThongKeHoaDons;
    }



}
