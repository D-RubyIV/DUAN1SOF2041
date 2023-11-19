/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raven.form;

import com.raven.component.EventPagination;
import com.raven.service.ChatLieuService;
import com.raven.service.HangService;
import com.raven.service.MauSacService;
import com.raven.service.SanPhamChiTietService;
import com.raven.service.SanPhamService;
import com.raven.service.SizeService;
import com.raven.style.PaginationItemRenderStyle1;
import com.raven.utils.XImage;
import com.ravent.entity.ChatLieu;
import com.ravent.entity.Hang;
import com.ravent.entity.MauSac;
import com.ravent.entity.SanPham;
import com.ravent.entity.SanPhamChiTiet;
import com.ravent.entity.Size;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author RAVEN
 */
public class Form_1 extends javax.swing.JPanel {

    /**
     * Creates new form Form_1
     */
    private SanPhamService sanPhamService = new SanPhamService();
    private SanPhamChiTietService sanPhamChiTietService = new SanPhamChiTietService();
    List<SanPham> listSanPham = new ArrayList<>();
    int INDEX_SELECTED_TBL_SANPHAM = -1;
    int CONFIG_LIMIT_DATA_PAGE = 3;
    int INDEX_SELECT_PAGE = 0;

    public Form_1() {
        initComponents();
        pagination1.setPaginationItemRender(new PaginationItemRenderStyle1());
        pagination1.setPagegination(1, 10);
        pagination1.addEventPagination(new EventPagination() {
            @Override
            public void pageChanged(int page) {
                INDEX_SELECT_PAGE = page - 1;
                System.out.println("PAGE CHANGE: " + INDEX_SELECT_PAGE);
                init_LoadSanPhamChiTietToTabble();
            }
        });
        init_LoadSanPhamChiTietToTabble();

    }

    public void init_LoadSanPhamChiTietToTabble() {
        listSanPham = sanPhamService.selectAllFromAToB(CONFIG_LIMIT_DATA_PAGE * INDEX_SELECT_PAGE, CONFIG_LIMIT_DATA_PAGE);
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Mã Sản Phẩm", "Tên Sản Phẩm", "Tổng Số Lượng", "Trạng Thái"});
        for (SanPham sanPham : listSanPham) {
            int tongSoLuong = sanPhamChiTietService.getTotalSanPhamChiTiet(sanPham.getMaSanPham());
            model.addRow(new Object[]{sanPham.getMaSanPham(), sanPham.getTenSanPham(), String.valueOf(tongSoLuong), tongSoLuong > 0 ? "Còn hàng" : "Hết hàng"});
        }
        tblBangSanPham.setModel(model);
    }

    public void showSanPhamChiTiet() {
        int maSanPham = Integer.valueOf(tblBangSanPham.getValueAt(INDEX_SELECTED_TBL_SANPHAM, 0).toString());
        DialogSanPhamChiTiet dialogSanPhamChiTiet = new DialogSanPhamChiTiet(new javax.swing.JFrame(), true, maSanPham);
        dialogSanPhamChiTiet.setVisible(true);
    }

    public void fillSanPhamToForm() {
        System.out.println("SELECTED ROW: " + INDEX_SELECTED_TBL_SANPHAM);
        String tenSanPham = tblBangSanPham.getValueAt(INDEX_SELECTED_TBL_SANPHAM, 1).toString();
        txtTenSanPham.setText(tenSanPham);
    }

    public void loadSanPham() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Mã Sản Phẩm", "Tên Sản Phẩm", "Tổng Số Lượng", "Trạng Thái"});

        listSanPham = sanPhamService.selectAllFromAToB(CONFIG_LIMIT_DATA_PAGE * INDEX_SELECT_PAGE, CONFIG_LIMIT_DATA_PAGE);
        for (SanPham sanPham : listSanPham) {

        }

        tblBangSanPham.setModel(model);
    }

    public void deleteSanPham() {
        if (INDEX_SELECTED_TBL_SANPHAM < 0) {
            showMessageBox("Vui lòng chọn 1 trong các sản phẩm trong bảng");
        } else {
            SanPham sanPham = listSanPham.get(INDEX_SELECTED_TBL_SANPHAM);
            showMessageBox(sanPhamService.delete(sanPham.getMaSanPham()));
            init_LoadSanPhamChiTietToTabble();
        }

    }

    public void addSanPham() {
        String tenSanPham = txtTenSanPham.getText();
        if (tenSanPham.isEmpty()) {
            showMessageBox("Vui lòng nhập tên sản phẩm");
        } else {
            showMessageBox(sanPhamService.add(tenSanPham));
            init_LoadSanPhamChiTietToTabble();
        }
    }

    public void updateSanPham() {
        if (INDEX_SELECTED_TBL_SANPHAM < 0) {
            showMessageBox("Vui lòng chọn 1 trong các sản phẩm trong bảng");
        } else {
            SanPham sanPham = listSanPham.get(INDEX_SELECTED_TBL_SANPHAM);
            String tenSanPham = txtTenSanPham.getText();
            sanPham.setTenSanPham(tenSanPham);
            if (tenSanPham.isEmpty()) {
                showMessageBox("Vui lòng nhập tên sản phẩm");
            } else {
                showMessageBox(sanPhamService.update(sanPham));
                init_LoadSanPhamChiTietToTabble();
            }

        }

    }

    public void showMessageBox(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public static String removeAccent(String input) {
        String normalized = java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD);
        normalized = normalized.replaceAll("[^\\p{ASCII}]", "");
        return normalized.toLowerCase();
    }

    //Load Các Thuộc Tính Lên    
//    public void loadThuocTinhToComboBoxTrongTabSanPhamChiTiet() {
//
//        //Load Thuộc Tính Hãng
//        List<Hang> listHang = hangService.selectAll();
//        DefaultComboBoxModel model_1 = new DefaultComboBoxModel();
//        for (Hang hang : listHang) {
//            model_1.addElement(hang);
//        }
//        cboHang.setModel(model_1);
//
//        //Load Thuộc Tính Màu Sắc
//        List<MauSac> listMauSac = mauSacService.selectAll();
//        DefaultComboBoxModel model_2 = new DefaultComboBoxModel();
//        for (MauSac mauSac : listMauSac) {
//            System.out.println(mauSac);
//            model_2.addElement(mauSac);
//        }
//        cboMauSac.setModel(model_2);
//
//        //Load Thuộc Tính Chất Liệu
//        List<ChatLieu> listChatLieu = chatLieuService.selectAll();
//        DefaultComboBoxModel model_3 = new DefaultComboBoxModel();
//        for (ChatLieu chatLieu : listChatLieu) {
//            model_3.addElement(chatLieu);
//        }
//        cboChatLieu.setModel(model_3);
//
//        //Load Thuộc Tính Hãng
//        List<Size> listSize = sizeService.selectAll();
//        DefaultComboBoxModel model_4 = new DefaultComboBoxModel();
//        for (Size size : listSize) {
//            model_4.addElement(size);
//        }
//        cboSize.setModel(model_4);
//
//        //Load Thuộc Tính Hãng
//        List<SanPham> listSanPham = sanPhamService.selectAll();
//        DefaultComboBoxModel model_5 = new DefaultComboBoxModel();
//        for (SanPham sanPham : listSanPham) {
//            model_5.addElement(sanPham);
//        }
//        cboSanPham.setModel(model_5);
//
//    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblBangSanPham = new javax.swing.JTable();
        btnThemSanPham = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        txtTenSanPham = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        pagination1 = new com.raven.component.Pagination();

        setBackground(new java.awt.Color(242, 242, 242));

        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        tblBangSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblBangSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblBangSanPhamMousePressed(evt);
            }
        });
        jScrollPane4.setViewportView(tblBangSanPham);

        btnThemSanPham.setText("Thêm");
        btnThemSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemSanPhamActionPerformed(evt);
            }
        });

        jButton2.setText("Sửa");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Xóa");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Làm mới");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel1.setText("Tên Sản Phẩm");

        jButton1.setText(" Xem Sản Phẩm Chi Tiết");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Tìm kiếm"));

        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });
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
                .addContainerGap()
                .addComponent(txtSearch)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pagination1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pagination1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTenSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1))
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(69, 69, 69)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(btnThemSanPham)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton2))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton4))))
                    .addComponent(jScrollPane4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnThemSanPham, jButton2, jButton3, jButton4});

        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThemSanPham)
                    .addComponent(jButton2)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTenSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton3)
                            .addComponent(jButton4)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Quản lý sản phẩm", jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 902, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        updateSanPham();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void tblBangSanPhamMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBangSanPhamMousePressed
        // TODO add your handling code here:
        INDEX_SELECTED_TBL_SANPHAM = tblBangSanPham.getSelectedRow();
        fillSanPhamToForm();
    }//GEN-LAST:event_tblBangSanPhamMousePressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        showSanPhamChiTiet();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnThemSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemSanPhamActionPerformed
        // TODO add your handling code here:
        addSanPham();
    }//GEN-LAST:event_btnThemSanPhamActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        deleteSanPham();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        // TODO add your handling code here:
        String searchText = txtSearch.getText();
        System.out.println("SEARCH TEXT: " + searchText);
        if (searchText.isEmpty()) {
            init_LoadSanPhamChiTietToTabble();
        } else {
            listSanPham.clear();
            for (SanPham sanPham : sanPhamService.selectAll()) {
                if (removeAccent(sanPham.getTenSanPham()).contains(searchText) || sanPham.getTenSanPham().contains(searchText)) {
                    listSanPham.add(sanPham);
                }
            }
            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new Object[]{"Mã Sản Phẩm", "Tên Sản Phẩm", "Tổng Số Lượng", "Trạng Thái"});
            for (SanPham sanPham : listSanPham) {
                int tongSoLuong = sanPhamChiTietService.getTotalSanPhamChiTiet(sanPham.getMaSanPham());
                model.addRow(new Object[]{sanPham.getMaSanPham(), sanPham.getTenSanPham(), String.valueOf(tongSoLuong), tongSoLuong > 0 ? "Còn hàng" : "Hết hàng"});
            }
            tblBangSanPham.setModel(model);
        }
    }//GEN-LAST:event_txtSearchKeyReleased

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        txtTenSanPham.setText("");
    }//GEN-LAST:event_jButton4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnThemSanPham;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private com.raven.component.Pagination pagination1;
    private javax.swing.JTable tblBangSanPham;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTenSanPham;
    // End of variables declaration//GEN-END:variables
}
