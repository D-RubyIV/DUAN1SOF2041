/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raven.form;

import com.raven.chart.ModelChart;
import com.raven.entity.DuLieuThongKeDoanhThu;
import com.raven.entity.DuLieuThongKeHoaDon;
import com.raven.entity.SanPham;
import com.raven.entity.SanPhamChiTiet;
import com.raven.service.DuLieuThongKeService;
import com.raven.service.SanPhamChiTietService;
import com.raven.service.SanPhamService;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author RAVEN
 */
public class Form_5 extends javax.swing.JPanel {

    /**
     * Creates new form Form_1
     */
    private DuLieuThongKeService duLieuThongKeService = new DuLieuThongKeService();
    private SanPhamService sanPhamService = new SanPhamService();
    private SanPhamChiTietService sanPhamChiTietService = new SanPhamChiTietService();

    public Form_5() {
        int year = 2023;
        int month = 2; // Tháng bắt đầu từ 0 (0 - tháng 1, 1 - tháng 2, ..., 11 - tháng 12)
        int day = 1;

        Date currentDate = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        // Lấy ra đối tượng Date từ Calendar
        Date date1 = calendar.getTime();

        initComponents();
        jdateChoseBegin.setDate(date1);
        jdateChoseBegin1.setDate(date1);
        jdateChoseEnd.setDate(new Date());
        jdateChoseEnd1.setDate(new Date());

        jdateChoseBegin.getDateEditor().addPropertyChangeListener(
                new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ("date".equals(e.getPropertyName())) {
                    System.out.println(e.getPropertyName()
                            + ": " + (Date) e.getNewValue());
                    Date date1 = jdateChoseBegin.getDate();
                    Date date2 = jdateChoseEnd.getDate();
                    if (date1.after(date2)) {
                        JOptionPane.showMessageDialog(Form_5.this, "Vui lòng chọn trước ngày hôm nay");
                        date1 = new Date();
                        calendar.add(Calendar.DAY_OF_MONTH, -30);
                        // Lấy ra đối tượng Date từ Calendar
                        Date dateNew = calendar.getTime();
                        jdateChoseBegin.setDate(dateNew);
                        return;
                    }
                    loadDataDoanhThuToBieuDo();
                }

            }
        });
        jdateChoseBegin1.getDateEditor().addPropertyChangeListener(
                new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ("date".equals(e.getPropertyName())) {
                    System.out.println(e.getPropertyName()
                            + ": " + (Date) e.getNewValue());
                    Date date1 = jdateChoseBegin1.getDate();
                    Date date2 = jdateChoseEnd1.getDate();
                    if (date1.after(date2)) {
                        JOptionPane.showMessageDialog(Form_5.this, "Vui lòng chọn trước ngày hôm nay");
                        date1 = new Date();
                        calendar.add(Calendar.DAY_OF_MONTH, -30);
                        // Lấy ra đối tượng Date từ Calendar
                        Date dateNew = calendar.getTime();
                        jdateChoseBegin1.setDate(dateNew);
                        return;
                    }
                    loadDataHoaDonToBieuDo();
                }

            }
        });
        jdateChoseEnd.getDateEditor().addPropertyChangeListener(
                new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ("date".equals(e.getPropertyName())) {
                    System.out.println(e.getPropertyName()
                            + ": " + (Date) e.getNewValue());
                    loadDataDoanhThuToBieuDo();
                }
            }
        });
        jdateChoseEnd1.getDateEditor().addPropertyChangeListener(
                new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ("date".equals(e.getPropertyName())) {
                    System.out.println(e.getPropertyName()
                            + ": " + (Date) e.getNewValue());
                    loadDataHoaDonToBieuDo();
                }
            }
        });
        addLegendInit();
        loadDataDoanhThuToBieuDo();
        loadDataHoaDonToBieuDo();
    }

    public void addLegendInit() {
        chart.addLegend("Expense", new Color(135, 189, 245));
    }

    public void loadDataHoaDonToBieuDo() {
        Date date1 = jdateChoseBegin1.getDate();
        Date date2 = jdateChoseEnd1.getDate();
        chart1.clear();
        chart1.addLegend("Hủy thanh toán", new Color(135, 189, 245));
        chart1.addLegend("Đã Thanh toán", new Color(189, 135, 245));
        // GET DU LIEU
        List<DuLieuThongKeHoaDon> lieuThongKeHoaDons = duLieuThongKeService.selectAllHoaDonByDate(date1, date2);
        Collections.sort(lieuThongKeHoaDons, new Comparator<DuLieuThongKeHoaDon>() {
            public int compare(DuLieuThongKeHoaDon o1, DuLieuThongKeHoaDon o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });

        // BY DAY
        int lenghtDuLieu = lieuThongKeHoaDons.size();
        int lenghCount = 0;
        Map<Integer, List<Integer>> mapDuLieuHoaDon = new HashMap<>();
        for (DuLieuThongKeHoaDon duLieuThongKeHoaDon : lieuThongKeHoaDons) {
            // Tạo đối tượng Calendar và đặt giá trị từ đối tượng Date
            Calendar calendarConvert = Calendar.getInstance();
            calendarConvert.setTime(duLieuThongKeHoaDon.getDate());
            // Lấy giá trị ngày từ Calendar
            int ngayLabel = calendarConvert.get(Calendar.DAY_OF_MONTH);
            int weekLabel = calendarConvert.get(Calendar.WEEK_OF_YEAR);
            int thangLabel = calendarConvert.get(Calendar.MONTH) + 1;
            int nameLabel = calendarConvert.get(Calendar.YEAR);

            int countDaThanhToan = duLieuThongKeHoaDon.getDaThanhToan();
            int countHuyThanhToan = duLieuThongKeHoaDon.getHuyThanhToan();

            if (rdoTheoNgay1.isSelected()) {
                System.out.println("BY NGAYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY");
                String timeLabel = String.format("%s/%s", ngayLabel, thangLabel);
                chart1.addData(new ModelChart(timeLabel, new double[]{duLieuThongKeHoaDon.getDaThanhToan(), duLieuThongKeHoaDon.getHuyThanhToan()}));
                lenghtDuLieu += 1;
            }
            if (rdoTheoTuan1.isSelected()) {
                System.out.println("BY TUANNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN");
                try {
                    List<Integer> duLieuHoaDon = mapDuLieuHoaDon.get(weekLabel);
                    mapDuLieuHoaDon.put(weekLabel, List.of(duLieuHoaDon.get(0) + countDaThanhToan, duLieuHoaDon.get(0) + countHuyThanhToan));
                } catch (Exception e) {
                    mapDuLieuHoaDon.put(weekLabel, List.of(countDaThanhToan, countHuyThanhToan));
                }
                System.out.println("DOANH THU TUAN" + weekLabel + ": " + mapDuLieuHoaDon.get(weekLabel));
            }
            if (rdoTheoThang1.isSelected()) {
                System.out.println("BY TUANNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN");
                try {
                    List<Integer> duLieuHoaDon = mapDuLieuHoaDon.get(thangLabel);
                    mapDuLieuHoaDon.put(thangLabel, List.of(duLieuHoaDon.get(0) + countDaThanhToan, duLieuHoaDon.get(0) + countHuyThanhToan));
                } catch (Exception e) {
                    mapDuLieuHoaDon.put(thangLabel, List.of(countDaThanhToan, countHuyThanhToan));
                }
                System.out.println("DOANH THU THANG" + thangLabel + ": " + mapDuLieuHoaDon.get(thangLabel));
            }

            if (rdoTheoNam1.isSelected()) {
                System.out.println("BY TUANNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN");
                try {
                    List<Integer> duLieuHoaDon = mapDuLieuHoaDon.get(nameLabel);
                    mapDuLieuHoaDon.put(nameLabel, List.of(duLieuHoaDon.get(0) + countDaThanhToan, duLieuHoaDon.get(0) + countHuyThanhToan));
                } catch (Exception e) {
                    mapDuLieuHoaDon.put(nameLabel, List.of(countDaThanhToan, countHuyThanhToan));
                }
                System.out.println("DOANH THU NAM" + nameLabel + ": " + mapDuLieuHoaDon.get(nameLabel));
            }

        }
        
        // Chuyển Map thành một danh sách các entry
        List<Map.Entry<Integer, List<Integer>>> entryList = new ArrayList<>(mapDuLieuHoaDon.entrySet());
        // Sắp xếp danh sách theo key
        entryList.sort(Comparator.comparing(Map.Entry::getKey));
        // Tạo một Map mới từ danh sách đã sắp xếp
        Map<Integer, List<Integer>> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, List<Integer>> entry : entryList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        

        if (rdoTheoTuan1.isSelected()) {
            for (Integer key : sortedMap.keySet()) {
                List<Integer> listValue = sortedMap.get(key);
                chart1.addData(new ModelChart(String.format("TUAN%s", key), new double[]{listValue.get(0), listValue.get(1)}));
            }
            lenghtDuLieu = sortedMap.keySet().size();
        }
        if (rdoTheoThang1.isSelected()) {
            for (Integer key : sortedMap.keySet()) {
                List<Integer> listValue = sortedMap.get(key);
                chart1.addData(new ModelChart(String.format("THANG%s", key), new double[]{listValue.get(0), listValue.get(1)}));
            }
            lenghtDuLieu = sortedMap.keySet().size();
        }
        if (rdoTheoNam1.isSelected()) {
            for (Integer key : sortedMap.keySet()) {
                List<Integer> listValue = sortedMap.get(key);
                chart1.addData(new ModelChart(String.format("NAM%s", key), new double[]{listValue.get(0), listValue.get(1)}));
            }
            lenghtDuLieu = sortedMap.keySet().size();
        }
        

        // Cuộn đến cuối
        int widthScroll = ((int) (lenghtDuLieu / 10)) * 800 + 500;
        chart1.setWidth(widthScroll);
        System.out.println("OK");
    }

    public void loadDataDoanhThuToBieuDo() {
        Date date1 = jdateChoseBegin.getDate();
        Date date2 = jdateChoseEnd.getDate();

        //chart.addLegend("Income", new Color(245, 189, 135));
        chart.clear();
        chart.addLegend("DoanhThu", new Color(135, 189, 245));
        //chart.addLegend("Doanh ", new Color(189, 135, 245));

        // GET SORT DATA
        List<DuLieuThongKeDoanhThu> listDuLieuThongKe = duLieuThongKeService.selectAllDoanhThuByDate(date1, date2);
        Collections.sort(listDuLieuThongKe, new Comparator<DuLieuThongKeDoanhThu>() {
            public int compare(DuLieuThongKeDoanhThu o1, DuLieuThongKeDoanhThu o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        int lenghtDuLieu = 0;
        Map<Integer, Integer> mapDuLieuDoanhThu = new HashMap<>();
        for (DuLieuThongKeDoanhThu duLieuThongKe : listDuLieuThongKe) {
            // Tạo đối tượng Calendar và đặt giá trị từ đối tượng Date
            Calendar calendarConvert = Calendar.getInstance();
            calendarConvert.setTime(duLieuThongKe.getDate());
            // Lấy giá trị ngày từ Calendar
            int ngayLabel = calendarConvert.get(Calendar.DAY_OF_MONTH);
            int weekLabel = calendarConvert.get(Calendar.WEEK_OF_YEAR);
            int thangLabel = calendarConvert.get(Calendar.MONTH) + 1;
            int nameLabel = calendarConvert.get(Calendar.YEAR);
            if (rdoTheoNgay.isSelected()) {
                System.out.println("BY NGAYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY");
                String timeLabel = String.format("%s/%s", ngayLabel, thangLabel);
                chart.addData(new ModelChart(timeLabel, new double[]{duLieuThongKe.getValue()}));
                lenghtDuLieu += 1;
            }
            if (rdoTheoTuan.isSelected()) {
                System.out.println("BY TUANNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN");
                try {
                    int doanhThuTuan = mapDuLieuDoanhThu.get(weekLabel);
                    mapDuLieuDoanhThu.put(weekLabel, doanhThuTuan + duLieuThongKe.getValue());
                } catch (Exception e) {
                    mapDuLieuDoanhThu.put(weekLabel, duLieuThongKe.getValue());
                }
                System.out.println("DOANH THU TUAN" + weekLabel + ": " + mapDuLieuDoanhThu.get(weekLabel));
            }
            if (rdoTheoThang.isSelected()) {
                System.out.println("BY THANGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
                try {
                    int doanhThuThang = mapDuLieuDoanhThu.get(thangLabel);
                    mapDuLieuDoanhThu.put(thangLabel, doanhThuThang + duLieuThongKe.getValue());
                } catch (Exception e) {
                    mapDuLieuDoanhThu.put(thangLabel, duLieuThongKe.getValue());
                }
                System.out.println("DOANH THU THANGS" + weekLabel + ": " + mapDuLieuDoanhThu.get(thangLabel));
            }
            if (rdoTheoNam.isSelected()) {
                System.out.println("BY NAMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
                try {
                    int doanhThuNam = mapDuLieuDoanhThu.get(nameLabel);
                    mapDuLieuDoanhThu.put(nameLabel, doanhThuNam + duLieuThongKe.getValue());
                } catch (Exception e) {
                    mapDuLieuDoanhThu.put(nameLabel, duLieuThongKe.getValue());
                }
                System.out.println("DOANH THU NAM" + nameLabel + ": " + mapDuLieuDoanhThu.get(nameLabel));
            }
        }
        
        
        // Chuyển Map thành một danh sách các entry
        List<Map.Entry<Integer, Integer>> entryList = new ArrayList<>(mapDuLieuDoanhThu.entrySet());
        // Sắp xếp danh sách theo key
        entryList.sort(Comparator.comparing(Map.Entry::getKey));
        // Tạo một Map mới từ danh sách đã sắp xếp
        Map<Integer, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, Integer> entry : entryList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        
        if (rdoTheoTuan.isSelected()) {
            for (Integer key : sortedMap.keySet()) {
                int val = sortedMap.get(key);
                chart.addData(new ModelChart(String.format("TUAN%s", key), new double[]{sortedMap.get(key)}));
            }
            lenghtDuLieu = sortedMap.keySet().size();
        }
        if (rdoTheoThang.isSelected()) {
            for (Integer key : sortedMap.keySet()) {
                int val = sortedMap.get(key);
                chart.addData(new ModelChart(String.format("THANG%s", key), new double[]{sortedMap.get(key)}));
            }
            lenghtDuLieu = sortedMap.keySet().size();
        }
        if (rdoTheoNam.isSelected()) {
            for (Integer key : sortedMap.keySet()) {
                int val = sortedMap.get(key);
                chart.addData(new ModelChart(String.format("%s", key), new double[]{sortedMap.get(key)}));
            }
            lenghtDuLieu = sortedMap.keySet().size();
        }
        // Cuộn đến cuối
        int widthScroll = ((int) (lenghtDuLieu / 10)) * 500 + 500;
        chart.setWidth(widthScroll);

        chart2.clear();
        chart2.addLegend("Sản Phẩm", new Color(135, 189, 245));
        List<SanPham> listSanPham = sanPhamService.selectAll();
        for (SanPham sanPham : listSanPham) {
            chart2.addData(new ModelChart(sanPham.getTenSanPham(), new double[]{sanPhamService.getCountSanPham(sanPham.getMaSanPham())}));
        }
        lenghtDuLieu = listSanPham.size();
        // Cuộn đến cuối
        widthScroll = ((int) (lenghtDuLieu / 10)) * 800 + 500;
        System.out.println("KHOANG CACH: " + widthScroll);
        chart2.setWidth(widthScroll);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jdateChoseBegin = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jdateChoseEnd = new com.toedter.calendar.JDateChooser();
        rdoTheoNgay = new javax.swing.JRadioButton();
        rdoTheoTuan = new javax.swing.JRadioButton();
        rdoTheoThang = new javax.swing.JRadioButton();
        rdoTheoNam = new javax.swing.JRadioButton();
        chart = new com.raven.chart.Chart();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jdateChoseBegin1 = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jdateChoseEnd1 = new com.toedter.calendar.JDateChooser();
        rdoTheoNgay1 = new javax.swing.JRadioButton();
        rdoTheoTuan1 = new javax.swing.JRadioButton();
        rdoTheoThang1 = new javax.swing.JRadioButton();
        rdoTheoNam1 = new javax.swing.JRadioButton();
        chart1 = new com.raven.chart.Chart();
        jPanel5 = new javax.swing.JPanel();
        chart2 = new com.raven.chart.Chart();

        setBackground(new java.awt.Color(242, 242, 242));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jdateChoseBegin.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdateChoseBeginPropertyChange(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Đến");

        jdateChoseEnd.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdateChoseEndPropertyChange(evt);
            }
        });

        buttonGroup1.add(rdoTheoNgay);
        rdoTheoNgay.setSelected(true);
        rdoTheoNgay.setText("Theo ngày");
        rdoTheoNgay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rdoTheoNgayMouseClicked(evt);
            }
        });

        buttonGroup1.add(rdoTheoTuan);
        rdoTheoTuan.setText("Theo tuần");
        rdoTheoTuan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rdoTheoTuanMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                rdoTheoTuanMousePressed(evt);
            }
        });

        buttonGroup1.add(rdoTheoThang);
        rdoTheoThang.setText("Theo tháng");
        rdoTheoThang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rdoTheoThangMouseClicked(evt);
            }
        });

        buttonGroup1.add(rdoTheoNam);
        rdoTheoNam.setText("Theo năm");
        rdoTheoNam.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rdoTheoNamMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rdoTheoNgay)
                .addGap(18, 18, 18)
                .addComponent(rdoTheoTuan)
                .addGap(27, 27, 27)
                .addComponent(rdoTheoThang)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rdoTheoNam)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 237, Short.MAX_VALUE)
                .addComponent(jdateChoseBegin, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jdateChoseEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jdateChoseBegin, jdateChoseEnd});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rdoTheoNgay)
                        .addComponent(rdoTheoTuan)
                        .addComponent(rdoTheoThang)
                        .addComponent(rdoTheoNam))
                    .addComponent(jdateChoseEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jdateChoseBegin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chart, javax.swing.GroupLayout.PREFERRED_SIZE, 1008, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chart, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Thống kê doanh thu", jPanel2);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jdateChoseBegin1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdateChoseBegin1PropertyChange(evt);
            }
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Đến");

        jdateChoseEnd1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdateChoseEnd1PropertyChange(evt);
            }
        });

        buttonGroup2.add(rdoTheoNgay1);
        rdoTheoNgay1.setSelected(true);
        rdoTheoNgay1.setText("Theo ngày");
        rdoTheoNgay1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rdoTheoNgay1MouseClicked(evt);
            }
        });

        buttonGroup2.add(rdoTheoTuan1);
        rdoTheoTuan1.setText("Theo tuần");
        rdoTheoTuan1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rdoTheoTuan1MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                rdoTheoTuan1MousePressed(evt);
            }
        });

        buttonGroup2.add(rdoTheoThang1);
        rdoTheoThang1.setText("Theo tháng");
        rdoTheoThang1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rdoTheoThang1MouseClicked(evt);
            }
        });

        buttonGroup2.add(rdoTheoNam1);
        rdoTheoNam1.setText("Theo năm");
        rdoTheoNam1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rdoTheoNam1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rdoTheoNgay1)
                .addGap(18, 18, 18)
                .addComponent(rdoTheoTuan1)
                .addGap(27, 27, 27)
                .addComponent(rdoTheoThang1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rdoTheoNam1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 239, Short.MAX_VALUE)
                .addComponent(jdateChoseBegin1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jdateChoseEnd1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rdoTheoNam1)
                        .addComponent(rdoTheoThang1)
                        .addComponent(rdoTheoTuan1)
                        .addComponent(rdoTheoNgay1))
                    .addComponent(jdateChoseEnd1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jdateChoseBegin1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chart1, javax.swing.GroupLayout.PREFERRED_SIZE, 1008, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 12, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chart1, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(119, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Thống kê hóa đơn", jPanel3);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chart2, javax.swing.GroupLayout.PREFERRED_SIZE, 1008, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(chart2, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(154, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Thống kê sản phẩm", jPanel5);

        add(jTabbedPane1, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void jdateChoseBeginPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jdateChoseBeginPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jdateChoseBeginPropertyChange

    private void jdateChoseEndPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jdateChoseEndPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jdateChoseEndPropertyChange

    private void jdateChoseBegin1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jdateChoseBegin1PropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jdateChoseBegin1PropertyChange

    private void jdateChoseEnd1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jdateChoseEnd1PropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jdateChoseEnd1PropertyChange

    private void rdoTheoTuanMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoTheoTuanMousePressed
        // TODO add your handling code here:


    }//GEN-LAST:event_rdoTheoTuanMousePressed

    private void rdoTheoTuanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoTheoTuanMouseClicked
        // TODO add your handling code here:
        loadDataDoanhThuToBieuDo();
    }//GEN-LAST:event_rdoTheoTuanMouseClicked

    private void rdoTheoNgayMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoTheoNgayMouseClicked
        // TODO add your handling code here:
        loadDataDoanhThuToBieuDo();
    }//GEN-LAST:event_rdoTheoNgayMouseClicked

    private void rdoTheoThangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoTheoThangMouseClicked
        // TODO add your handling code here:
        loadDataDoanhThuToBieuDo();
    }//GEN-LAST:event_rdoTheoThangMouseClicked

    private void rdoTheoNamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoTheoNamMouseClicked
        // TODO add your handling code here:
        loadDataDoanhThuToBieuDo();
    }//GEN-LAST:event_rdoTheoNamMouseClicked

    private void rdoTheoNgay1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoTheoNgay1MouseClicked
        // TODO add your handling code here:
        loadDataHoaDonToBieuDo();
    }//GEN-LAST:event_rdoTheoNgay1MouseClicked

    private void rdoTheoTuan1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoTheoTuan1MouseClicked
        // TODO add your handling code here:
        loadDataHoaDonToBieuDo();
    }//GEN-LAST:event_rdoTheoTuan1MouseClicked

    private void rdoTheoTuan1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoTheoTuan1MousePressed
        // TODO add your handling code here:

    }//GEN-LAST:event_rdoTheoTuan1MousePressed

    private void rdoTheoThang1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoTheoThang1MouseClicked
        // TODO add your handling code here:
        loadDataHoaDonToBieuDo();
    }//GEN-LAST:event_rdoTheoThang1MouseClicked

    private void rdoTheoNam1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoTheoNam1MouseClicked
        // TODO add your handling code here:
        loadDataHoaDonToBieuDo();
    }//GEN-LAST:event_rdoTheoNam1MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private com.raven.chart.Chart chart;
    private com.raven.chart.Chart chart1;
    private com.raven.chart.Chart chart2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private com.toedter.calendar.JDateChooser jdateChoseBegin;
    private com.toedter.calendar.JDateChooser jdateChoseBegin1;
    private com.toedter.calendar.JDateChooser jdateChoseEnd;
    private com.toedter.calendar.JDateChooser jdateChoseEnd1;
    private javax.swing.JRadioButton rdoTheoNam;
    private javax.swing.JRadioButton rdoTheoNam1;
    private javax.swing.JRadioButton rdoTheoNgay;
    private javax.swing.JRadioButton rdoTheoNgay1;
    private javax.swing.JRadioButton rdoTheoThang;
    private javax.swing.JRadioButton rdoTheoThang1;
    private javax.swing.JRadioButton rdoTheoTuan;
    private javax.swing.JRadioButton rdoTheoTuan1;
    // End of variables declaration//GEN-END:variables
}
