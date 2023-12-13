/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raven.form;

import com.raven.component.EventPagination;
import com.raven.service.NguoiDungService;
import com.raven.service.VaitroService;
import com.raven.style.PaginationItemRenderStyle1;
import com.raven.entity.NguoiDung;
import com.raven.entity.VaiTro;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author RAVEN
 */
public class Form_4 extends javax.swing.JPanel {

    /**
     * Creates new form Form_1
     */
    VaitroService vaitroService = new VaitroService();
    NguoiDungService nguoiDungService = new NguoiDungService();
    List<NguoiDung> listNguoiDung = new ArrayList<>();
    int INDEX_SELECT_PAGE = 1;
    int CONFIG_LIMIT_DATA_PAGE = 5;

    public Form_4() {
        initComponents();
        loadVaiTroToComboBox();
        loadVaiTroToComboBoxLoc();
        loadNguoiDungToTable();

        pagination1.setPaginationItemRender(new PaginationItemRenderStyle1());
        pagination1.addEventPagination(new EventPagination() {
            @Override
            public void pageChanged(int page) {
                INDEX_SELECT_PAGE = page;
                loadNguoiDungToTable();
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
            // Lấy đường dẫn của thư mục được chọn
            String selectedDirectoryPath = fileChooser.getSelectedFile().getAbsolutePath();
            System.out.println("Đường dẫn thư mục đã chọn: " + selectedDirectoryPath);
            List<NguoiDung> listNguoiDungExport = new ArrayList<>();
            if (cboLocVaiTro.getSelectedIndex() != 0) {
                VaiTro vaiTro = ((VaiTro) cboLocVaiTro.getSelectedItem());
                listNguoiDungExport = nguoiDungService.selectAllByIdVaiTro(vaiTro.getMaVaitro());
            } else {
                listNguoiDungExport = nguoiDungService.selectAll();
            }
            JOptionPane.showMessageDialog(this, exportCustomersToExcel(listNguoiDungExport, selectedDirectoryPath + "\\DSKH.xlsx"));
        } else {
            System.out.println("Người dùng đã hủy bỏ việc chọn thư mục.");
        }
    }

    public void loadVaiTroToComboBox() {
        List<VaiTro> listVaiTro = vaitroService.selectAll();
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (VaiTro vaiTro : listVaiTro) {
            model.addElement(vaiTro);
        }
        cboVaiTro.setModel(model);
    }

    public void loadVaiTroToComboBoxLoc() {
        List<VaiTro> listVaiTro = vaitroService.selectAll();
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement("Tất cả");
        for (VaiTro vaiTro : listVaiTro) {
            model.addElement(vaiTro);
        }
        cboLocVaiTro.setModel(model);
    }

    public void loadNguoiDungToTable() {
        int totalItem = 0;
        String textSearch = txtTimKiem.getText().toLowerCase();
        String baseSql = "SELECT * FROM NGUOIDUNG";
        if (!textSearch.isEmpty() || cboLocVaiTro.getSelectedIndex() != 0) {

            if (!textSearch.isEmpty()) {
                baseSql += " WHERE ( TENNGUOIDUNG LIKE N'%" + textSearch + "%' "
                        + "OR SODIENTHOAI LIKE N'%" + textSearch + "%' "
                        + "OR TENTAIKHOAN LIKE N'%" + textSearch + "%' "
                        + "OR EMAIL LIKE N'%" + textSearch + "%')";
            }
            // BY Loc
            if (cboLocVaiTro.getSelectedIndex() != 0) {
                VaiTro vaiTroSelected = ((VaiTro) cboLocVaiTro.getSelectedItem());
                if (!textSearch.isEmpty() && cboLocVaiTro.getSelectedIndex() != 0) {
                    baseSql += String.format(" AND MAVAITRO = %s", vaiTroSelected.getMaVaitro());
                } else {
                    baseSql += String.format(" WHERE MAVAITRO = %s", vaiTroSelected.getMaVaitro());

                }
                totalItem = nguoiDungService.selectAllByIdVaiTro(vaiTroSelected.getMaVaitro()).size();
            }

        } else {
            totalItem = nguoiDungService.selectAll().size();
        }

        baseSql += String.format(" ORDER BY MANGUOIDUNG OFFSET %s ROWS FETCH NEXT %s ROWS ONLY ;", CONFIG_LIMIT_DATA_PAGE * (INDEX_SELECT_PAGE - 1), CONFIG_LIMIT_DATA_PAGE);
        System.out.println("BASE SQL: " + baseSql);
        listNguoiDung = nguoiDungService.selectAllByCustomSql(baseSql);

        DefaultTableModel defaultTableModel = new DefaultTableModel();
        defaultTableModel.setColumnIdentifiers(new Object[]{"Ma người dùng", "Tên người dùng", "Tên Vai Trò", "SĐT", "Email", "Tên tài khoản", "Mật khẩu"});
        for (NguoiDung nguoiDung : listNguoiDung) {
            String tenVaiTro = vaitroService.findById(nguoiDung.getMaVaiTro()).getTenVaitro();
            defaultTableModel.addRow(new Object[]{nguoiDung.getMaNguoiDung(), nguoiDung.getTenNguoiDung(), tenVaiTro, nguoiDung.getSoDienThoai(), nguoiDung.getEmail(),
                nguoiDung.getTenTaiKhoan(), nguoiDung.getMatKhau()});
        }
        tblNguoiDung.setModel(defaultTableModel);
        pagination1.setPagegination(INDEX_SELECT_PAGE, (int) Math.ceil((float) totalItem / CONFIG_LIMIT_DATA_PAGE));
    }

    public void clearForm() {
        txtTenNguoiDung.setText("");
        txtSoDienThoai.setText("");
        txtEmail.setText("");
        txtTenTaiKhoan.setText("");
        txtMatKhau.setText("");
    }

    private void callDialogVaiTro() {
        DialogVaiTro dialogVaiTro = new DialogVaiTro(new javax.swing.JFrame(), true);
        dialogVaiTro.setVisible(true);
        dialogVaiTro.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("Dialog đã đóng");
                loadVaiTroToComboBox();
                loadVaiTroToComboBoxLoc();
            }
        });
    }

    public void addEntity() {

        NguoiDung nguoiDung = getObj();
        
        if (nguoiDungService.findByTenTaiKhoan(nguoiDung.getTenTaiKhoan()) != null) {
            showMessageBox("Tên tài khoản đã tồn tại");
            return;
        }
        
        if (nguoiDung != null) {
            showMessageBox(nguoiDungService.add(nguoiDung));
            loadNguoiDungToTable();
        }
    }

    public void loadDataToForm() {
        NguoiDung nguoiDung = listNguoiDung.get(tblNguoiDung.getSelectedRow());
        txtTenNguoiDung.setText(nguoiDung.getTenNguoiDung());
        txtEmail.setText(nguoiDung.getEmail());
        txtSoDienThoai.setText(nguoiDung.getSoDienThoai());
        txtTenTaiKhoan.setText(nguoiDung.getTenTaiKhoan());
        txtMatKhau.setText(nguoiDung.getMatKhau());
        DefaultComboBoxModel<VaiTro> modelVaiTro = (DefaultComboBoxModel) cboVaiTro.getModel();
        for (int i = 0; i < modelVaiTro.getSize(); i++) {
            if (modelVaiTro.getElementAt(i).getMaVaitro() == nguoiDung.getMaVaiTro()) {
                cboVaiTro.setSelectedIndex(i);
                break;
            }
        }
    }

    public String exportCustomersToExcel(List<NguoiDung> customerList, String filePath) {
        System.out.println(customerList);
        try (Workbook workbook = new XSSFWorkbook()) {
            // Tạo một bảng tính mới
            Sheet sheet = workbook.createSheet("Customer Data");

            // Tạo hàng tiêu đề
            Row headerRow = sheet.createRow(0);
            String[] columns = {"Mã người dùng", "Tên người dùng", "Số điẹn thoại", "Email"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Đổ dữ liệu từ danh sách khách hàng vào bảng tính
            int rowNum = 1;
            for (NguoiDung customer : customerList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(customer.getMaNguoiDung());
                row.createCell(1).setCellValue(customer.getTenNguoiDung());
                row.createCell(2).setCellValue(String.valueOf(customer.getSoDienThoai()));
                row.createCell(3).setCellValue(customer.getEmail());
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

    public void deleteEntity() {
        NguoiDung nguoiDung = listNguoiDung.get(tblNguoiDung.getSelectedRow());
        showMessageBox(nguoiDungService.delete(nguoiDung.getMaNguoiDung()));
        loadNguoiDungToTable();
    }

    public void editEntity() {
        NguoiDung nguoiDung = getObj();
        if (nguoiDung != null) {
            showMessageBox(nguoiDungService.update(nguoiDung));
            loadNguoiDungToTable();
        }
    }

    public void showMessageBox(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public NguoiDung getObj() {
        NguoiDung nd = new NguoiDung();
        String tenNguoiDung = txtTenNguoiDung.getText();
        String soDienThoai = txtSoDienThoai.getText();
        String email = txtEmail.getText();
        String tenTaiKhoan = txtTenTaiKhoan.getText();
        String matkhau = txtMatKhau.getText();
        VaiTro vaiTro = ((VaiTro) cboVaiTro.getSelectedItem());

        if (tenNguoiDung.isEmpty()) {
            showMessageBox("Vui Lòng nhập tên người dùng");
            return null;
        }
        if (soDienThoai.isEmpty()) {
            showMessageBox("Vui Lòng nhập số điện thoại");
            return null;
        }
        if (email.isEmpty()) {
            showMessageBox("Vui Lòng nhập số lượng email");
            return null;
        }
        if (tenTaiKhoan.isEmpty()) {
            showMessageBox("Vui Lòng nhập tên tài khoản");
            return null;
        }
        if (matkhau.isEmpty()) {
            showMessageBox("Vui Lòng nhập matkhau");
            return null;
        }

        return new NguoiDung(WIDTH, vaiTro.getMaVaitro(), tenNguoiDung, soDienThoai, email, tenTaiKhoan, matkhau);
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
        tblNguoiDung = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        cboLocVaiTro = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        txtTimKiem = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        btnCapNhat = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        txtSoDienThoai = new javax.swing.JTextField();
        txtMatKhau = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtTenTaiKhoan = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtTenNguoiDung = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cboVaiTro = new javax.swing.JComboBox<>();
        btnThem = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        btnXoa = new javax.swing.JButton();
        txtEmail = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        pagination1 = new com.raven.component.Pagination();
        jButton1 = new javax.swing.JButton();

        setBackground(new java.awt.Color(242, 242, 242));

        tblNguoiDung.setModel(new javax.swing.table.DefaultTableModel(
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
        tblNguoiDung.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblNguoiDungMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblNguoiDung);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Lọc"));
        jPanel1.setToolTipText("");

        cboLocVaiTro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboLocVaiTro.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboLocVaiTroItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cboLocVaiTro, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cboLocVaiTro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Tìm kiếm"));
        jPanel2.setToolTipText("");

        txtTimKiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKiemKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin người dùng"));

        jLabel5.setText("Mật khẩu");

        btnCapNhat.setText("Làm mới");
        btnCapNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatActionPerformed(evt);
            }
        });

        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        jLabel1.setText("Tên người dùng:");

        jLabel2.setText("Số điện thoại");

        jLabel6.setText("Vai trò");

        jLabel3.setText("Email");

        cboVaiTro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboVaiTro.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboVaiTroItemStateChanged(evt);
            }
        });

        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        jLabel4.setText("Tên tài khoản");

        btnXoa.setText("Xóa");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        jButton5.setText("+");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtEmail))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSoDienThoai))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTenNguoiDung, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cboVaiTro, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addComponent(txtTenTaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnThem)
                        .addGap(18, 18, 18)
                        .addComponent(btnSua))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnXoa)
                        .addGap(18, 18, 18)
                        .addComponent(btnCapNhat)))
                .addGap(28, 28, 28))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnCapNhat, btnSua, btnThem, btnXoa});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtTenNguoiDung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(txtTenTaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtSoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(txtMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(cboVaiTro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton5)))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnThem)
                                .addComponent(btnSua))
                            .addGap(34, 34, 34))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnXoa)
                            .addComponent(btnCapNhat))))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(102, 102, 102));

        pagination1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel4.add(pagination1);

        jButton1.setText("Xuất danh sách khách hàng");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(12, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnCapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatActionPerformed
        // TODO add your handling code here:
        clearForm();
    }//GEN-LAST:event_btnCapNhatActionPerformed

    private void cboVaiTroItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboVaiTroItemStateChanged
        // TODO add your handling code here:
        VaiTro vaiTro = ((VaiTro) cboVaiTro.getSelectedItem());
    }//GEN-LAST:event_cboVaiTroItemStateChanged

    private void cboLocVaiTroItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboLocVaiTroItemStateChanged
        // TODO add your handling code here:
        loadNguoiDungToTable();
    }//GEN-LAST:event_cboLocVaiTroItemStateChanged

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        callDialogVaiTro();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void txtTimKiemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemKeyReleased
        // TODO add your handling code here:
        loadNguoiDungToTable();
    }//GEN-LAST:event_txtTimKiemKeyReleased

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        addEntity();
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
        deleteEntity();
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
        editEntity();
    }//GEN-LAST:event_btnSuaActionPerformed

    private void tblNguoiDungMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNguoiDungMousePressed
        // TODO add your handling code here:
        loadDataToForm();
    }//GEN-LAST:event_tblNguoiDungMousePressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        // Tạo một đối tượng JFileChooser
        exportExcel();


    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCapNhat;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cboLocVaiTro;
    private javax.swing.JComboBox<String> cboVaiTro;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private com.raven.component.Pagination pagination1;
    private javax.swing.JTable tblNguoiDung;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtMatKhau;
    private javax.swing.JTextField txtSoDienThoai;
    private javax.swing.JTextField txtTenNguoiDung;
    private javax.swing.JTextField txtTenTaiKhoan;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
