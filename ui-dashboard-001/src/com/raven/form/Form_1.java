/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raven.form;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.raven.component.EventPagination;
import com.raven.component.Pagination;
import com.raven.service.ChatLieuService;
import com.raven.service.HangService;
import com.raven.service.MauSacService;
import com.raven.service.SanPhamChiTietService;
import com.raven.service.SanPhamService;
import com.raven.service.SizeService;
import com.raven.style.PaginationItemRenderStyle1;
import com.raven.utils.XImage;
import com.raven.entity.ChatLieu;
import com.raven.entity.Hang;
import com.raven.entity.MauSac;
import com.raven.entity.SanPham;
import com.raven.entity.SanPhamChiTiet;
import com.raven.entity.Size;
import static com.raven.form.DialogSanPhamChiTiet.createQR;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
    private HangService hangService = new HangService();
    private MauSacService mauSacService = new MauSacService();
    private ChatLieuService chatLieuService = new ChatLieuService();
    private SizeService sizeService = new SizeService();

    List<SanPham> listSanPham = new ArrayList<>();
    int INDEX_SELECTED_TBL_SANPHAM = -1;
    int CONFIG_LIMIT_DATA_PAGE = 5;
    int INDEX_SELECT_PAGE = 1;

    public Form_1() {
        initComponents();
        pagination1.setPaginationItemRender(new PaginationItemRenderStyle1());
        pagination1.addEventPagination(new EventPagination() {
            @Override
            public void pageChanged(int page) {
                INDEX_SELECT_PAGE = page;
                System.out.println("PAGE CHANGE: " + INDEX_SELECT_PAGE);
                init_LoadSanPhamChiTietToTabble();
            }
        });
        init_LoadSanPhamChiTietToTabble();
    }

    public void init_LoadSanPhamChiTietToTabble() {
        pagination1.setPagegination(INDEX_SELECT_PAGE, (int) Math.ceil((float) sanPhamService.selectAll().size() / CONFIG_LIMIT_DATA_PAGE));
        listSanPham = sanPhamService.selectAllFromAToB(CONFIG_LIMIT_DATA_PAGE * (INDEX_SELECT_PAGE - 1), CONFIG_LIMIT_DATA_PAGE);
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Mã Sản Phẩm", "Tên Sản Phẩm", "Tổng Số Lượng", "Trạng Thái"});
        for (SanPham sanPham : listSanPham) {
            int tongSoLuong = sanPhamChiTietService.getTotalSanPhamChiTiet(sanPham.getMaSanPham());
            model.addRow(new Object[]{sanPham.getMaSanPham(), sanPham.getTenSanPham(), String.valueOf(tongSoLuong), tongSoLuong > 0 ? "Còn hàng" : "Hết hàng"});
        }
        tblBangSanPham.setModel(model);
        lbTongSanPham.setText("Tổng Sản Phẩm: " + sanPhamService.selectAll().size());
    }

    public void showSanPhamChiTiet() {
        int maSanPham = Integer.valueOf(tblBangSanPham.getValueAt(INDEX_SELECTED_TBL_SANPHAM, 0).toString());
        DialogSanPhamChiTiet dialogSanPhamChiTiet = new DialogSanPhamChiTiet(new javax.swing.JFrame(), true, maSanPham);
        dialogSanPhamChiTiet.setVisible(true);

        dialogSanPhamChiTiet.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("Dialog đã đóng");
                init_LoadSanPhamChiTietToTabble();
            }
        });
    }

    public void exportExcel() {
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        // Chỉ cho phép chọn thư mục, không chọn file
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        // Hiển thị hộp thoại chọn thư mục và kiểm tra xem người dùng đã chọn thư mục hay không
        int returnValue = fileChooser.showDialog(null, "Chọn thư mục");

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                // Lấy đường dẫn của thư mục được chọn
                String selectedDirectoryPath = fileChooser.getSelectedFile().getAbsolutePath();
                System.out.println("Đường dẫn thư mục đã chọn: " + selectedDirectoryPath);
                List<SanPhamChiTiet> listSanPhamChiTiets = sanPhamChiTietService.selectAll();
                JOptionPane.showMessageDialog(this, exportProductsToExcel(listSanPhamChiTiets, selectedDirectoryPath + "\\FULL_SPCT.xlsx"));
            } catch (WriterException ex) {
                Logger.getLogger(DialogSanPhamChiTiet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("Người dùng đã hủy bỏ việc chọn thư mục.");
        }
    }

    public String exportProductsToExcel(List<SanPhamChiTiet> listSanPhamChiTiets, String filePath) throws WriterException {
        try (Workbook workbook = new XSSFWorkbook()) {
            // Tạo một bảng tính mới
            Sheet sheet = workbook.createSheet("Customer Data");

            // Tạo hàng tiêu đề
            Row headerRow = sheet.createRow(0);
            String[] columns = {"Mã sản phầm chi tiết", "Tên sản phẩm", "Tên hãng", "Tên màu sắc", "Tên chất liệu", "Tên size", "Số lượng", "Giá"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Đổ dữ liệu từ danh sách khách hàng vào bảng tính
            int rowNum = 1;
            for (SanPhamChiTiet sanPhamChiTiet : listSanPhamChiTiets) {
                Row row = sheet.createRow(rowNum++);
                String tenSanPham = sanPhamService.findById(sanPhamChiTiet.getMaSanPham()).getTenSanPham();
                String tenHang = hangService.findById(sanPhamChiTiet.getMaHang()).getTenHang();
                String tenMauSac = mauSacService.findById(sanPhamChiTiet.getMaMauSac()).getTenMauSac();
                String tenSize = sizeService.findById(sanPhamChiTiet.getMaSize()).getTenSize();
                String tenChatLieu = chatLieuService.findById(sanPhamChiTiet.getMaChatLieu()).getTenChatLieu();

                row.createCell(0).setCellValue(sanPhamChiTiet.getMaSanPhamChiTiet());
                row.createCell(1).setCellValue(tenSanPham);
                row.createCell(2).setCellValue(tenHang);
                row.createCell(3).setCellValue(tenMauSac);
                row.createCell(4).setCellValue(tenChatLieu);
                row.createCell(5).setCellValue(tenSize);
                row.createCell(6).setCellValue(String.valueOf(sanPhamChiTiet.getSoLuong()));
                row.createCell(7).setCellValue(sanPhamChiTiet.getGiaSanPham());

                String codeGenQR = String.format("HONIESNEAKERSPCT_%s", sanPhamChiTiet.getMaSanPhamChiTiet());
                // The path where the image will get saved
                String path = String.format("/HonieSneacker/QrCode/Product/QR%s.png", codeGenQR);
                // Encoding charset
                String charset = "UTF-8";
                Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
                hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
                createQR(codeGenQR, path, charset, hashMap, 200, 200);

            }

            // Lưu tệp Excel
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
                System.out.println("Excel file exported successfully!");
                return "Xuất file thành công!";
            }

        } catch (IOException e) {
            System.out.println(e);;
        }
        return "Xuất file thất bại!";
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
        jButton4 = new javax.swing.JButton();
        txtTenSanPham = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        pagination1 = new com.raven.component.Pagination();
        jLabel3 = new javax.swing.JLabel();
        lbTongSanPham = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();

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
            .addComponent(txtSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));
        jPanel2.setForeground(new java.awt.Color(204, 204, 204));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        pagination1.setOpaque(false);
        jPanel2.add(pagination1, new java.awt.GridBagConstraints());
        jPanel2.add(jLabel3, new java.awt.GridBagConstraints());

        lbTongSanPham.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTongSanPham.setText("Tổng sản phẩm:");
        jPanel2.add(lbTongSanPham, new java.awt.GridBagConstraints());

        jButton3.setText(" Xuất Toàn Bộ Danh Sách Sản Phẩm");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jScrollPane4))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtTenSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addComponent(btnThemSanPham)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton2)
                                .addGap(37, 37, 37)
                                .addComponent(jButton4))
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnThemSanPham, jButton2, jButton4});

        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTenSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThemSanPham)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Quản lý sản phẩm", jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
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
        init_LoadSanPhamChiTietToTabble();
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
        init_LoadSanPhamChiTietToTabble();
    }//GEN-LAST:event_btnThemSanPhamActionPerformed

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

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        exportExcel();
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnThemSanPham;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lbTongSanPham;
    private com.raven.component.Pagination pagination1;
    private javax.swing.JTable tblBangSanPham;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTenSanPham;
    // End of variables declaration//GEN-END:variables
}
