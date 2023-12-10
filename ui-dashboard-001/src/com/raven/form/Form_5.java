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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
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
        System.out.println(date1);

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
        // By Thang
        List<DuLieuThongKeHoaDon> lieuThongKeHoaDons = duLieuThongKeService.selectAllHoaDonByDate(date1, date2);
        Collections.sort(lieuThongKeHoaDons, new Comparator<DuLieuThongKeHoaDon>() {
            public int compare(DuLieuThongKeHoaDon o1, DuLieuThongKeHoaDon o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        int lenghtDuLieu = lieuThongKeHoaDons.size();
        for (DuLieuThongKeHoaDon duLieuThongKeHoaDon : lieuThongKeHoaDons) {
            // Tạo đối tượng Calendar và đặt giá trị từ đối tượng Date
            Calendar calendarConvert = Calendar.getInstance();
            calendarConvert.setTime(duLieuThongKeHoaDon.getDate());
            // Lấy giá trị ngày từ Calendar
            int ngayLabel = calendarConvert.get(Calendar.DAY_OF_MONTH);
            int thangLabel = calendarConvert.get(Calendar.MONTH) + 1;
            int nameLabel = calendarConvert.get(Calendar.YEAR);
            String timeLabel = String.format("%s/%s", ngayLabel, thangLabel);
            chart1.addData(new ModelChart(timeLabel, new double[]{duLieuThongKeHoaDon.getDaThanhToan(), duLieuThongKeHoaDon.getHuyThanhToan()}));
        }
        // Cuộn đến cuối
        int widthScroll = ((int) (lenghtDuLieu / 10)) * 800 + 500;
        System.out.println("KHOANG CACH: " + widthScroll);
        chart1.setWidth(widthScroll);
    }

    public void loadDataDoanhThuToBieuDo() {
        Date date1 = jdateChoseBegin.getDate();
        Date date2 = jdateChoseEnd.getDate();

        //chart.addLegend("Income", new Color(245, 189, 135));
        chart.clear();
        chart.addLegend("DoanhThu", new Color(135, 189, 245));
        //chart.addLegend("Doanh ", new Color(189, 135, 245));

        // By Thang
        List<DuLieuThongKeDoanhThu> listDuLieuThongKe = duLieuThongKeService.selectAllDoanhThuByDate(date1, date2);
        Collections.sort(listDuLieuThongKe, new Comparator<DuLieuThongKeDoanhThu>() {
            public int compare(DuLieuThongKeDoanhThu o1, DuLieuThongKeDoanhThu o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        int lenghtDuLieu = listDuLieuThongKe.size();
        for (DuLieuThongKeDoanhThu duLieuThongKe : listDuLieuThongKe) {
            // Tạo đối tượng Calendar và đặt giá trị từ đối tượng Date
            Calendar calendarConvert = Calendar.getInstance();
            calendarConvert.setTime(duLieuThongKe.getDate());
            // Lấy giá trị ngày từ Calendar
            int ngayLabel = calendarConvert.get(Calendar.DAY_OF_MONTH);
            int thangLabel = calendarConvert.get(Calendar.MONTH) + 1;
            int nameLabel = calendarConvert.get(Calendar.YEAR);
            String timeLabel = String.format("%s/%s", ngayLabel, thangLabel);
            chart.addData(new ModelChart(timeLabel, new double[]{duLieuThongKe.getValue()}));

        }
        // Cuộn đến cuối
        int widthScroll = ((int) (lenghtDuLieu / 10)) * 500 + 500;
        System.out.println("KHOANG CACH: " + widthScroll);
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

//        chart.addData(new ModelChart("January", new double[]{500, 200, 80}));
//        chart.addData(new ModelChart("February", new double[]{600, 750, 90}));
//        chart.addData(new ModelChart("March", new double[]{200, 350, 460}));
//        chart.addData(new ModelChart("April", new double[]{480, 150, 750}));
//        chart.addData(new ModelChart("May", new double[]{350, 540, 300}));
//        chart.addData(new ModelChart("June", new double[]{190, 280, 81}));
//        chart.addData(new ModelChart("June", new double[]{190, 280, 81}));
//        chart.addData(new ModelChart("June", new double[]{190, 280, 81}));
//        chart.addData(new ModelChart("June", new double[]{190, 280, 81}));
//        chart.addData(new ModelChart("June", new double[]{190, 280, 81}));
//        chart.addData(new ModelChart("June", new double[]{190, 280, 81}));
//        chart.addData(new ModelChart("June", new double[]{190, 280, 81}));
//        chart.addData(new ModelChart("June", new double[]{190, 280, 81}));
//        chart.addData(new ModelChart("June", new double[]{190, 280, 81}));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jdateChoseBegin = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jdateChoseEnd = new com.toedter.calendar.JDateChooser();
        chart = new com.raven.chart.Chart();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jdateChoseBegin1 = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jdateChoseEnd1 = new com.toedter.calendar.JDateChooser();
        chart1 = new com.raven.chart.Chart();
        jPanel5 = new javax.swing.JPanel();
        chart2 = new com.raven.chart.Chart();

        setBackground(new java.awt.Color(242, 242, 242));
        setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.BorderLayout());

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(613, Short.MAX_VALUE)
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
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jdateChoseEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jdateChoseBegin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel1, java.awt.BorderLayout.PAGE_START);
        jPanel2.add(chart, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Thống kê doanh thu", jPanel2);

        jPanel3.setLayout(new java.awt.BorderLayout());

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

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(615, Short.MAX_VALUE)
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
                .addGap(13, 13, 13)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jdateChoseEnd1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jdateChoseBegin1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel4, java.awt.BorderLayout.PAGE_START);
        jPanel3.add(chart1, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Thống kê hóa đơn", jPanel3);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1008, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(chart2, javax.swing.GroupLayout.PREFERRED_SIZE, 1008, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 437, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(chart2, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Thống kê sản phẩm", jPanel5);

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
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
    // End of variables declaration//GEN-END:variables
}
