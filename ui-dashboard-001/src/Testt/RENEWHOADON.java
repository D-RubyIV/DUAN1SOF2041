/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Testt;

import com.raven.entity.HoaDon;
import com.raven.entity.HoaDonChiTiet;
import com.raven.entity.NguoiDung;
import com.raven.entity.SanPhamChiTiet;
import com.raven.service.HoaDonChiTietService;
import com.raven.service.HoaDonService;
import com.raven.service.NguoiDungService;
import com.raven.service.SanPhamChiTietService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author phamh
 */
public class RENEWHOADON {

    public static void main(String[] args) {
        HoaDonService hoaDonService = new HoaDonService();
        HoaDonChiTietService hoaDonChiTietService = new HoaDonChiTietService();
        SanPhamChiTietService sanPhamChiTietService = new SanPhamChiTietService();

        List<HoaDonChiTiet> listHoaDonChiTiet = new ArrayList<>();
        listHoaDonChiTiet = hoaDonChiTietService.selectAll();
        List<Integer> listMaHoaDon = new ArrayList<>();
        for (HoaDonChiTiet hoaDonChiTiet : listHoaDonChiTiet) {
            listMaHoaDon.add(hoaDonChiTiet.getMaHoaDon());
        }
        Set<Integer> uniqueSet = new HashSet<>(listMaHoaDon);
        listMaHoaDon = new ArrayList<>(uniqueSet);
        
        for (Integer integer : listMaHoaDon) {
            System.out.println(integer);
            int maHoaDon = integer;
            List<NguoiDung> listNguoiDungs = new NguoiDungService().selectAllByIdVaiTro(3);
            Random random = new Random();
            int randomIndex = random.nextInt(listNguoiDungs.size());
            NguoiDung nguoiDung = listNguoiDungs.get(randomIndex);

            List<HoaDonChiTiet> listHoaDonChiTiets = hoaDonChiTietService.selectAllByMaHoaDon(maHoaDon);
            float tongTienTruocGiam = 0;
            for (HoaDonChiTiet hoaDonChiTiet : listHoaDonChiTiets) {
                SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietService.findById(hoaDonChiTiet.getMaSanPhamChiTiet());
                tongTienTruocGiam += sanPhamChiTiet.getGiaSanPham() * hoaDonChiTiet.getSoLuong();
            }

            // Sinh số nguyên ngẫu nhiên từ 1 đến 100
            int randomNumber = random.nextInt(60) + 1;
            Date currentDate = new Date();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(currentDate);
            // Trừ đi 7 ngày từ ngày hiện tại
            calendar.add(Calendar.DAY_OF_MONTH, -randomNumber);

            // Lấy ngày mới
            Date sevenDaysAgo = calendar.getTime();

            HoaDon hoaDonFound = hoaDonService.findById(maHoaDon);
            HoaDon hoaDon = new HoaDon(maHoaDon, 2, nguoiDung.getMaNguoiDung(), null, sevenDaysAgo, sevenDaysAgo, tongTienTruocGiam, tongTienTruocGiam, 0, 3);
            System.out.println(hoaDon);
            if (hoaDonFound != null) {
                hoaDonService.update(hoaDon);
            } else {
                hoaDonService.add(hoaDon);
            }

        }
    }

}
