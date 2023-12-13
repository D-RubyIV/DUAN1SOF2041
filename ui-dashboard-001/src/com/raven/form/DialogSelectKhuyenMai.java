/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.raven.form;

import com.raven.service.HoaDonService;
import com.raven.service.KhuyenMaiService;
import com.raven.entity.HoaDon;
import com.raven.entity.KhuyenMai;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author phamh
 */
public class DialogSelectKhuyenMai extends javax.swing.JDialog {

    /**
     * Creates new form DialogSelectMaGiamGia
     */
    KhuyenMaiService khuyenMaiService = new KhuyenMaiService();
    int TRANGTHAI = 0;
    int GLOBAL_MAHOADON = 0;
    HoaDonService hoaDonService = new HoaDonService();
    List<KhuyenMai> listKhuyenMai = new ArrayList<>();

    public DialogSelectKhuyenMai(java.awt.Frame parent, boolean modal, int maHoaDon) {
        super(parent, modal);
        GLOBAL_MAHOADON = maHoaDon;
        initComponents();
        setTitle("Chọn khuyến mãi cho hóa đơn: " + maHoaDon);
        setLocationRelativeTo(null);
        loadTrangThaiToComboBox();
        loadDataToBang();
    }

    private void loadDataToBang() {
        TRANGTHAI = cboLoc.getSelectedIndex();
        String searchText = txtSearch.getText().toLowerCase();
        if (searchText.isEmpty()) {
            listKhuyenMai = khuyenMaiService.selectAll();
        } else {
            String customSql = "SELECT * FROM KHUYENMAI"
                    + " WHERE MAKHUYENMAI LIKE N'%" + searchText + "%' "
                    + "OR TENKHUYENMAI LIKE N'%" + searchText + "%' ";
            System.out.println(customSql);
            listKhuyenMai = khuyenMaiService.selectAllByCustomSql(customSql);
        }

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Mã khuyến mãi", "Tên khuyến mãi", "Só tiền giẩm", "Só lượng", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái"});
        for (KhuyenMai khuyenMai : listKhuyenMai) {
            Date ngayHetHan = khuyenMai.getNgayKetThuc();
            System.out.println(ngayHetHan);
            System.out.println(ngayHetHan.before(new Date()));
            Date today = new Date();
            String trangThai = ngayHetHan.after(today) == true ? "Còn hạn" : "Hết hạn";
            System.out.println(trangThai);
            if (TRANGTHAI == 0) {
                model.addRow(new Object[]{khuyenMai.getMaKhuyenMai(), khuyenMai.getTenKhuyenMai(), khuyenMai.getMenhGia(), khuyenMai.getSoLuong(), khuyenMai.getNgayBatDau(), khuyenMai.getNgayKetThuc(), trangThai});
            } else {
                int codeTrangThai = ngayHetHan.before(new Date()) ? 1 : 2;
                if (TRANGTHAI == 1) {
                    if (codeTrangThai == 2) {
                        model.addRow(new Object[]{khuyenMai.getMaKhuyenMai(), khuyenMai.getTenKhuyenMai(), khuyenMai.getMenhGia(), khuyenMai.getSoLuong(), khuyenMai.getNgayBatDau(), khuyenMai.getNgayKetThuc(), trangThai});
                    }
                } else {
                    if (codeTrangThai == 1) {
                        model.addRow(new Object[]{khuyenMai.getMaKhuyenMai(), khuyenMai.getTenKhuyenMai(), khuyenMai.getMenhGia(), khuyenMai.getSoLuong(), khuyenMai.getNgayBatDau(), khuyenMai.getNgayKetThuc(), trangThai});
                    }
                }
            }
        }
        tblKhuyenMai.setModel(model);
    }

    private void xacNhanKhuyenMai() {
        HoaDon hoaDon = hoaDonService.findById(GLOBAL_MAHOADON);
        String maKhuyenMaiSelect = tblKhuyenMai.getValueAt(tblKhuyenMai.getSelectedRow(), 0).toString();
        KhuyenMai khuyenMai = khuyenMaiService.findById(maKhuyenMaiSelect);
        if (khuyenMai.getNgayKetThuc().after(new Date()) && khuyenMai.getSoLuong() > 0) {
            hoaDon.setMaGiamGia(maKhuyenMaiSelect);
            JOptionPane.showMessageDialog(this, hoaDonService.updateMaKhuyeMai(hoaDon));
            this.dispose();
        } else if (khuyenMai.getSoLuong() == 0) {
            JOptionPane.showMessageDialog(this, "Khuyến mãi đã hết lượt dùng");
        } else {
            JOptionPane.showMessageDialog(this, "Khuyến mãi này đã hết hạn");
        }

    }

    private void loadTrangThaiToComboBox() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement("Tất cả");
        model.addElement("Còn hạn");
        model.addElement("Hết hạn");
        cboLoc.setModel(model);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblKhuyenMai = new javax.swing.JTable();
        btnXacNhan = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        cboLoc = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tblKhuyenMai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblKhuyenMai);

        btnXacNhan.setText("Xác nhận");
        btnXacNhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXacNhanActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Tìm kiếm"));

        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 8, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Lọc"));

        cboLoc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboLoc.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboLocItemStateChanged(evt);
            }
        });
        cboLoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLocActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cboLoc, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cboLoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(btnXacNhan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnXacNhan)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cboLocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLocActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboLocActionPerformed

    private void cboLocItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboLocItemStateChanged
        // TODO add your handling code here:
        loadDataToBang();
    }//GEN-LAST:event_cboLocItemStateChanged

    private void btnXacNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXacNhanActionPerformed
        // TODO add your handling code here:
        xacNhanKhuyenMai();
    }//GEN-LAST:event_btnXacNhanActionPerformed

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        // TODO add your handling code here:
        loadDataToBang();
    }//GEN-LAST:event_txtSearchKeyReleased

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnXacNhan;
    private javax.swing.JComboBox<String> cboLoc;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblKhuyenMai;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
