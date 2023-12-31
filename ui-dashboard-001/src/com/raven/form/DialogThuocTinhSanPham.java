/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.raven.form;

import com.raven.service.ChatLieuService;
import com.raven.service.HangService;
import com.raven.service.MauSacService;
import com.raven.service.SanPhamChiTietService;
import com.raven.service.SanPhamService;
import com.raven.service.SizeService;
import com.raven.entity.ChatLieu;
import com.raven.entity.Hang;
import com.raven.entity.MauSac;
import com.raven.entity.SanPham;
import com.raven.entity.SanPhamChiTiet;
import com.raven.entity.Size;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author phamh
 */
public class DialogThuocTinhSanPham extends javax.swing.JDialog {

    /**
     * Creates new form DialogThuocTinhSanPham
     */
    private HangService hangService = new HangService();
    private MauSacService mauSacService = new MauSacService();
    private ChatLieuService chatLieuService = new ChatLieuService();
    private SizeService sizeService = new SizeService();
    private SanPhamService sanPhamService = new SanPhamService();
    private SanPhamChiTietService sanPhamChiTietService = new SanPhamChiTietService();
    List<SanPhamChiTiet> listSanPhamChiTiet = new ArrayList<>();
    List<SanPham> listSanPham = new ArrayList<>();
    int INDEX_SELECT_ROW_BANG_THUOC_TINH = -1;

    String GLOBAL_SELECTED_THUOCTINH = "Hãng";

    public DialogThuocTinhSanPham(java.awt.Frame parent, boolean modal, String tenThuocTinh) {
        super(parent, modal);
        GLOBAL_SELECTED_THUOCTINH = tenThuocTinh;
        initComponents();
        setLocationRelativeTo(null);
        loadBangThuocTinhTrongTabThuocTinh();
    }

    public void showMessageBox(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void clearFormThuocTinh() {
        txtTenThuocTinh.setText("");
    }

    public void loadBangThuocTinhTrongTabThuocTinh() {
        INDEX_SELECT_ROW_BANG_THUOC_TINH = -1;
        clearFormThuocTinh();
        DefaultTableModel model = new DefaultTableModel();
        if (GLOBAL_SELECTED_THUOCTINH.equals("Hãng") == true) {
            model.setColumnIdentifiers(new Object[]{"Mã Hãng", "Tên Hãng"});
            List<Hang> listHang = hangService.selectAll();
            for (Hang hang : listHang) {
                model.addRow(new Object[]{hang.getMaHang(), hang.getTenHang()});
            }
        } else if (GLOBAL_SELECTED_THUOCTINH.equals("Màu Sắc") == true) {
            model.setColumnIdentifiers(new Object[]{"Mã Màu Sắc", "Tên Màu Sắc"});
            List<MauSac> listMauSac = mauSacService.selectAll();
            for (MauSac mauSac : listMauSac) {
                model.addRow(new Object[]{mauSac.getMaMauSac(), mauSac.getTenMauSac()});
            }
        } else if (GLOBAL_SELECTED_THUOCTINH.equals("Chất Liệu") == true) {
            model.setColumnIdentifiers(new Object[]{"Mã Chất Liệu", "Tên Chất Liệu"});
            List<ChatLieu> listChatList = chatLieuService.selectAll();
            for (ChatLieu chatLieu : listChatList) {
                model.addRow(new Object[]{chatLieu.getMaChatLieu(), chatLieu.getTenChatLieu()});
            }
        } else if (GLOBAL_SELECTED_THUOCTINH.equals("Size") == true) {
            model.setColumnIdentifiers(new Object[]{"Mã Size", "Tên Size"});
            List<Size> listSize = sizeService.selectAll();
            for (Size size : listSize) {
                model.addRow(new Object[]{size.getMaSize(), size.getTenSize()});
            }
        }
        tblBangThuocTinh.setModel(model);

    }

    public void fillDataBangThuocTinhToForm() {
        INDEX_SELECT_ROW_BANG_THUOC_TINH = tblBangThuocTinh.getSelectedRow();
        String tenThuocTinh = tblBangThuocTinh.getValueAt(INDEX_SELECT_ROW_BANG_THUOC_TINH, 1).toString();
        txtTenThuocTinh.setText(tenThuocTinh);
        System.out.println("Row Select: " + INDEX_SELECT_ROW_BANG_THUOC_TINH);

    }

    public void addThuocTinh() {
        String tenThuocTinh = txtTenThuocTinh.getText();
        if (tenThuocTinh.isEmpty()) {
            showMessageBox("Vui lòng nhập tên thuộc tính");
        } else {
            if (GLOBAL_SELECTED_THUOCTINH.equals("Hãng") == true) {
                showMessageBox(hangService.add(tenThuocTinh));
            } else if (GLOBAL_SELECTED_THUOCTINH.equals("Màu Sắc") == true) {
                showMessageBox(mauSacService.add(tenThuocTinh));
            } else if (GLOBAL_SELECTED_THUOCTINH.equals("Chất Liệu") == true) {
                showMessageBox(chatLieuService.add(tenThuocTinh));
            } else if (GLOBAL_SELECTED_THUOCTINH.equals("Size") == true) {
                showMessageBox(sizeService.add(tenThuocTinh));
            } else {
                showMessageBox("Vui lòng chọn 1 trong các thuộc tính");
            }
            loadBangThuocTinhTrongTabThuocTinh();
        }
    }

    public void deleteThuocTinh() {

        if (INDEX_SELECT_ROW_BANG_THUOC_TINH <= 0) {
            showMessageBox("Vui lòng chọn 1 trong các thuộc tính trong bảng");
            return;
        }
        int maThuocTinh = Integer.valueOf(tblBangThuocTinh.getValueAt(INDEX_SELECT_ROW_BANG_THUOC_TINH, 0).toString());
        int choice = JOptionPane.showConfirmDialog(this, "Bạn có muốn xóa thuộc tính này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (choice == 0) {
            if (GLOBAL_SELECTED_THUOCTINH.equals("Hãng") == true) {
                showMessageBox(hangService.delete(maThuocTinh));
            } else if (GLOBAL_SELECTED_THUOCTINH.equals("Màu Sắc") == true) {
                showMessageBox(mauSacService.delete(maThuocTinh));
            } else if (GLOBAL_SELECTED_THUOCTINH.equals("Chất Liệu") == true) {
                showMessageBox(chatLieuService.delete(maThuocTinh));
            } else if (GLOBAL_SELECTED_THUOCTINH.equals("Size") == true) {
                showMessageBox(sizeService.delete(maThuocTinh));
            }
            loadBangThuocTinhTrongTabThuocTinh();
        }

    }

    public void updateThuocTinh() {
        
        
        if (INDEX_SELECT_ROW_BANG_THUOC_TINH <= 0) {
            showMessageBox("Vui lòng chọn 1 trong các thuộc tính trong bảng");
            return;
        }
        int indexRowBangThuocTinhSelect = tblBangThuocTinh.getSelectedRow();
        int maThuocTinh = Integer.valueOf(tblBangThuocTinh.getValueAt(indexRowBangThuocTinhSelect, 0).toString());
        String tenThuocTinh = txtTenThuocTinh.getText();
        if (GLOBAL_SELECTED_THUOCTINH.equals("Hãng") == true) {
            showMessageBox(hangService.update(new Hang(maThuocTinh, tenThuocTinh)));
        } else if (GLOBAL_SELECTED_THUOCTINH.equals("Màu Sắc") == true) {
            showMessageBox(mauSacService.update(new MauSac(maThuocTinh, tenThuocTinh)));
        } else if (GLOBAL_SELECTED_THUOCTINH.equals("Chất Liệu") == true) {
            showMessageBox(chatLieuService.update(new ChatLieu(maThuocTinh, tenThuocTinh)));
        } else if (GLOBAL_SELECTED_THUOCTINH.equals("Size") == true) {
            showMessageBox(sizeService.update(new Size(maThuocTinh, tenThuocTinh)));
        }
        loadBangThuocTinhTrongTabThuocTinh();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtTenThuocTinh = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        btnAddThuocTinh = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblBangThuocTinh = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel6.setText("Tên thuộc tính");

        btnAddThuocTinh.setText("Thêm");
        btnAddThuocTinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddThuocTinhActionPerformed(evt);
            }
        });

        btnClear.setText("Làm mới");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnEdit.setText("Sửa");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        tblBangThuocTinh.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblBangThuocTinh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblBangThuocTinhMousePressed(evt);
            }
        });
        jScrollPane3.setViewportView(tblBangThuocTinh);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTenThuocTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAddThuocTinh)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClear)))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAddThuocTinh, btnClear, btnEdit});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTenThuocTinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(btnAddThuocTinh)
                    .addComponent(btnEdit)
                    .addComponent(btnClear))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddThuocTinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddThuocTinhActionPerformed
        // TODO add your handling code here:
        addThuocTinh();
    }//GEN-LAST:event_btnAddThuocTinhActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        clearFormThuocTinh();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        updateThuocTinh();
    }//GEN-LAST:event_btnEditActionPerformed

    private void tblBangThuocTinhMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBangThuocTinhMousePressed
        // TODO add your handling code here:
        fillDataBangThuocTinhToForm();
    }//GEN-LAST:event_tblBangThuocTinhMousePressed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddThuocTinh;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnEdit;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tblBangThuocTinh;
    private javax.swing.JTextField txtTenThuocTinh;
    // End of variables declaration//GEN-END:variables
}
