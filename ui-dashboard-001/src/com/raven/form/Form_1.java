/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raven.form;

import com.raven.service.ChatLieuService;
import com.raven.service.HangService;
import com.raven.service.MauSacService;
import com.raven.service.SanPhamService;
import com.raven.service.SizeService;
import com.ravent.entity.ChatLieu;
import com.ravent.entity.Hang;
import com.ravent.entity.MauSac;
import com.ravent.entity.SanPham;
import com.ravent.entity.Size;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
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
    private HangService hangService = new HangService();
    private MauSacService mauSacService = new MauSacService();
    private ChatLieuService chatLieuService = new ChatLieuService();
    private SizeService sizeService = new SizeService();
    private SanPhamService sanPhamService = new SanPhamService();
    int INDEX_SELECT_ROW_BANG_THUOC_TINH = -1;

    public void showMessageBox(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void clearFormThuocTinh() {
        txtTenThuocTinh.setText("");
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
            if (rdoHang.isSelected()) {
                showMessageBox(hangService.add(tenThuocTinh));
            } else if (rdoMauSac.isSelected()) {
                showMessageBox(mauSacService.add(tenThuocTinh));
            } else if (rdoChatLieu.isSelected()) {
                showMessageBox(chatLieuService.add(tenThuocTinh));
            } else if (rdoSize.isSelected()) {
                showMessageBox(sizeService.add(tenThuocTinh));
            } else if (rdoSize.isSelected()) {
                showMessageBox(sizeService.add(tenThuocTinh));
            } else if (rdoSanPham.isSelected()) {
                showMessageBox(sanPhamService.add(tenThuocTinh));
            } else {
                showMessageBox("Vui lòng chọn 1 trong các thuộc tính");
            }
            loadBangThuocTinhTrongTabThuocTinh();
        }
    }

    public void deleteThuocTinh() {

        if (rdoHang.isSelected() || rdoChatLieu.isSelected() || rdoMauSac.isSelected() || rdoSanPham.isSelected() || rdoSize.isSelected()) {
            if (INDEX_SELECT_ROW_BANG_THUOC_TINH <= 0) {
                showMessageBox("Vui lòng chọn 1 trong các thuộc tính trong bảng");
                return;
            }
            int maThuocTinh = Integer.valueOf(tblBangThuocTinh.getValueAt(INDEX_SELECT_ROW_BANG_THUOC_TINH, 0).toString());
            int choice = JOptionPane.showConfirmDialog(this, "Bạn có muốn xóa thuộc tính này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (choice == 0) {
                if (rdoHang.isSelected()) {
                    showMessageBox(hangService.delete(maThuocTinh));
                } else if (rdoMauSac.isSelected()) {
                    showMessageBox(mauSacService.delete(maThuocTinh));
                } else if (rdoChatLieu.isSelected()) {
                    showMessageBox(chatLieuService.delete(maThuocTinh));
                } else if (rdoSize.isSelected()) {
                    showMessageBox(sizeService.delete(maThuocTinh));
                } else if (rdoSize.isSelected()) {
                    showMessageBox(sizeService.delete(maThuocTinh));
                } else if (rdoSanPham.isSelected()) {
                    showMessageBox(sanPhamService.delete(maThuocTinh));
                }
                loadBangThuocTinhTrongTabThuocTinh();
            }
        } else {
            showMessageBox("Vui lòng chọn 1 trong các thuộc tính");
        }
    }

    public void updateThuocTinh() {
        if (rdoHang.isSelected() || rdoChatLieu.isSelected() || rdoMauSac.isSelected() || rdoSanPham.isSelected() || rdoSize.isSelected()) {
            if (INDEX_SELECT_ROW_BANG_THUOC_TINH <= 0) {
                showMessageBox("Vui lòng chọn 1 trong các thuộc tính trong bảng");
                return;
            }
            int indexRowBangThuocTinhSelect = tblBangThuocTinh.getSelectedRow();
            int maThuocTinh = Integer.valueOf(tblBangThuocTinh.getValueAt(indexRowBangThuocTinhSelect, 0).toString());
            String tenThuocTinh = tblBangThuocTinh.getValueAt(indexRowBangThuocTinhSelect, 1).toString();
            if (rdoHang.isSelected()) {
                showMessageBox(hangService.update(new Hang(maThuocTinh, tenThuocTinh)));
            } else if (rdoMauSac.isSelected()) {
                showMessageBox(mauSacService.update(new MauSac(maThuocTinh, tenThuocTinh)));
            } else if (rdoChatLieu.isSelected()) {
                showMessageBox(chatLieuService.update(new ChatLieu(maThuocTinh, tenThuocTinh)));
            } else if (rdoSize.isSelected()) {
                showMessageBox(sizeService.update(new Size(maThuocTinh, tenThuocTinh)));
            } else if (rdoSanPham.isSelected()) {
                showMessageBox(sanPhamService.update(new SanPham(maThuocTinh, tenThuocTinh)));
            }
            loadBangThuocTinhTrongTabThuocTinh();
        } else {
            showMessageBox("Vui lòng chọn 1 trong các thuộc tính");
        }
    }

    public void loadBangThuocTinhTrongTabThuocTinh() {
        INDEX_SELECT_ROW_BANG_THUOC_TINH = -1;
        clearFormThuocTinh();
        DefaultTableModel model = new DefaultTableModel();
        if (rdoHang.isSelected()) {
            model.setColumnIdentifiers(new Object[]{"Mã Hãng", "Tên Hãng"});
            List<Hang> listHang = hangService.selectAll();
            for (Hang hang : listHang) {
                model.addRow(new Object[]{hang.getMaHang(), hang.getTenHang()});
            }
        } else if (rdoMauSac.isSelected()) {
            model.setColumnIdentifiers(new Object[]{"Mã Màu Sắc", "Tên Màu Sắc"});
            List<MauSac> listMauSac = mauSacService.selectAll();
            for (MauSac mauSac : listMauSac) {
                model.addRow(new Object[]{mauSac.getMaMauSac(), mauSac.getTenMauSac()});
            }
        } else if (rdoChatLieu.isSelected()) {
            model.setColumnIdentifiers(new Object[]{"Mã Chất Liệu", "Tên Chất Liệu"});
            List<ChatLieu> listChatList = chatLieuService.selectAll();
            for (ChatLieu chatLieu : listChatList) {
                model.addRow(new Object[]{chatLieu.getMaChatLieu(), chatLieu.getTenChatLieu()});
            }
        } else if (rdoSize.isSelected()) {
            model.setColumnIdentifiers(new Object[]{"Mã Size", "Tên Size"});
            List<Size> listSize = sizeService.selectAll();
            for (Size size : listSize) {
                model.addRow(new Object[]{size.getMaSize(), size.getTenSize()});
            }
        } else if (rdoSanPham.isSelected()) {
            model.setColumnIdentifiers(new Object[]{"Mã Sản Phẩm", "Tên Sản Phẩm"});
            List<SanPham> listSanPham = sanPhamService.selectAll();
            for (SanPham sanPham : listSanPham) {
                model.addRow(new Object[]{sanPham.getMaSanPham(), sanPham.getTenSanPham()});
            }
        }
        tblBangThuocTinh.setModel(model);

    }

    //Load Các Thuộc Tính Lên    
    public void loadThuocTinhToComboBoxTrongTabSanPhamChiTiet() {

        //Load Thuộc Tính Hãng
        List<Hang> listHang = hangService.selectAll();
        DefaultComboBoxModel model_1 = new DefaultComboBoxModel();
        for (Hang hang : listHang) {
            model_1.addElement(hang);
        }
        cboHang.setModel(model_1);

        //Load Thuộc Tính Màu Sắc
        List<MauSac> listMauSac = mauSacService.selectAll();
        DefaultComboBoxModel model_2 = new DefaultComboBoxModel();
        for (MauSac mauSac : listMauSac) {
            System.out.println(mauSac);
            model_2.addElement(mauSac);
        }
        cboMauSac.setModel(model_2);

        //Load Thuộc Tính Chất Liệu
        List<ChatLieu> listChatLieu = chatLieuService.selectAll();
        DefaultComboBoxModel model_3 = new DefaultComboBoxModel();
        for (ChatLieu chatLieu : listChatLieu) {
            model_3.addElement(chatLieu);
        }
        cboChatLieu.setModel(model_3);

        //Load Thuộc Tính Hãng
        List<Size> listSize = sizeService.selectAll();
        DefaultComboBoxModel model_4 = new DefaultComboBoxModel();
        for (Size size : listSize) {
            model_4.addElement(size);
        }
        cboSize.setModel(model_4);

        //Load Thuộc Tính Hãng
        List<SanPham> listSanPham = sanPhamService.selectAll();
        DefaultComboBoxModel model_5 = new DefaultComboBoxModel();
        for (SanPham sanPham : listSanPham) {
            model_5.addElement(sanPham);
        }
        cboSanPham.setModel(model_5);

    }

    public Form_1() {
        initComponents();
        loadThuocTinhToComboBoxTrongTabSanPhamChiTiet();
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtGia = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtMoTa = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblBangSanPhamChiTiet = new javax.swing.JTable();
        cboChatLieu = new javax.swing.JComboBox<>();
        cboSanPham = new javax.swing.JComboBox<>();
        cboSize = new javax.swing.JComboBox<>();
        cboHang = new javax.swing.JComboBox<>();
        cboMauSac = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        txtSoLuong = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        btnAddThuocTinh = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblBangThuocTinh = new javax.swing.JTable();
        rdoMauSac = new javax.swing.JRadioButton();
        rdoHang = new javax.swing.JRadioButton();
        rdoChatLieu = new javax.swing.JRadioButton();
        rdoSize = new javax.swing.JRadioButton();
        txtTenThuocTinh = new javax.swing.JTextField();
        rdoSanPham = new javax.swing.JRadioButton();
        btnClear = new javax.swing.JButton();

        setBackground(new java.awt.Color(242, 242, 242));

        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jLabel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel2.setText("Sản Phẩm");

        jLabel3.setText("Màu Sắc");

        jLabel4.setText("Hãng:");

        jLabel5.setText("Chất liệu:");

        jLabel7.setText("Giá");

        jLabel8.setText("Size:");

        jLabel9.setText("Mô Tả");

        txtGia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGiaActionPerformed(evt);
            }
        });

        txtMoTa.setColumns(20);
        txtMoTa.setRows(5);
        jScrollPane1.setViewportView(txtMoTa);

        tblBangSanPhamChiTiet.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tblBangSanPhamChiTiet);

        cboChatLieu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cboSanPham.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cboSize.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cboHang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cboMauSac.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel10.setText("Số Lượng");

        txtSoLuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSoLuongActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 867, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cboSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cboMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(cboHang, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboSize, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cboChatLieu, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(cboSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(cboSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(cboMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(cboHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cboChatLieu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(8, 8, 8)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(79, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Quản lý sản phẩm chi tiết", jPanel1);

        jLabel6.setText("Tên thuộc tính");

        btnAddThuocTinh.setText("Thêm");
        btnAddThuocTinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddThuocTinhActionPerformed(evt);
            }
        });

        btnEdit.setText("Sửa");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnDelete.setText("Xóa");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
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

        buttonGroup1.add(rdoMauSac);
        rdoMauSac.setText("Màu sắc");
        rdoMauSac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoMauSacActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdoHang);
        rdoHang.setText("Hãng");
        rdoHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoHangActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdoChatLieu);
        rdoChatLieu.setText("Chất liệu");
        rdoChatLieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoChatLieuActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdoSize);
        rdoSize.setText("Size");
        rdoSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSizeActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdoSanPham);
        rdoSanPham.setText("Sản phẩm");
        rdoSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rdoSanPhamMouseClicked(evt);
            }
        });

        btnClear.setText("Làm mới");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(127, 127, 127)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rdoSanPham))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(rdoHang)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rdoMauSac)
                                .addGap(29, 29, 29)
                                .addComponent(rdoChatLieu)
                                .addGap(18, 18, 18)
                                .addComponent(rdoSize))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTenThuocTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAddThuocTinh)
                            .addComponent(btnDelete))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEdit)
                            .addComponent(btnClear))))
                .addContainerGap(123, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {rdoChatLieu, rdoHang, rdoMauSac, rdoSanPham, rdoSize});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAddThuocTinh, btnClear, btnDelete, btnEdit});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTenThuocTinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(btnAddThuocTinh)
                    .addComponent(btnEdit))
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdoSanPham)
                    .addComponent(rdoHang)
                    .addComponent(rdoMauSac)
                    .addComponent(rdoChatLieu)
                    .addComponent(rdoSize)
                    .addComponent(btnDelete)
                    .addComponent(btnClear))
                .addGap(48, 48, 48)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(96, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Quản lý thuộc tính", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 898, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(7, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtGiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGiaActionPerformed

    private void txtSoLuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSoLuongActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSoLuongActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        updateThuocTinh();
    }//GEN-LAST:event_btnEditActionPerformed

    private void rdoMauSacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoMauSacActionPerformed
        // TODO add your handling code here:
        loadBangThuocTinhTrongTabThuocTinh();
    }//GEN-LAST:event_rdoMauSacActionPerformed

    private void rdoChatLieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoChatLieuActionPerformed
        // TODO add your handling code here:
        loadBangThuocTinhTrongTabThuocTinh();
    }//GEN-LAST:event_rdoChatLieuActionPerformed

    private void btnAddThuocTinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddThuocTinhActionPerformed
        // TODO add your handling code here:
        addThuocTinh();
    }//GEN-LAST:event_btnAddThuocTinhActionPerformed

    private void rdoSanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoSanPhamMouseClicked
        // TODO add your handling code here:
        loadBangThuocTinhTrongTabThuocTinh();
    }//GEN-LAST:event_rdoSanPhamMouseClicked

    private void rdoHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoHangActionPerformed
        // TODO add your handling code here:
        loadBangThuocTinhTrongTabThuocTinh();
    }//GEN-LAST:event_rdoHangActionPerformed

    private void rdoSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSizeActionPerformed
        // TODO add your handling code here:
        loadBangThuocTinhTrongTabThuocTinh();
    }//GEN-LAST:event_rdoSizeActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        deleteThuocTinh();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void tblBangThuocTinhMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBangThuocTinhMousePressed
        // TODO add your handling code here:
        fillDataBangThuocTinhToForm();
    }//GEN-LAST:event_tblBangThuocTinhMousePressed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        clearFormThuocTinh();
    }//GEN-LAST:event_btnClearActionPerformed

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        // TODO add your handling code here:
        loadThuocTinhToComboBoxTrongTabSanPhamChiTiet();
        loadBangThuocTinhTrongTabThuocTinh();
    }//GEN-LAST:event_jTabbedPane1StateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddThuocTinh;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboChatLieu;
    private javax.swing.JComboBox<String> cboHang;
    private javax.swing.JComboBox<String> cboMauSac;
    private javax.swing.JComboBox<String> cboSanPham;
    private javax.swing.JComboBox<String> cboSize;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JRadioButton rdoChatLieu;
    private javax.swing.JRadioButton rdoHang;
    private javax.swing.JRadioButton rdoMauSac;
    private javax.swing.JRadioButton rdoSanPham;
    private javax.swing.JRadioButton rdoSize;
    private javax.swing.JTable tblBangSanPhamChiTiet;
    private javax.swing.JTable tblBangThuocTinh;
    private javax.swing.JTextField txtGia;
    private javax.swing.JTextArea txtMoTa;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtTenThuocTinh;
    // End of variables declaration//GEN-END:variables
}
