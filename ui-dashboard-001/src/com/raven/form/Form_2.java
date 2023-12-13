/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raven.form;

import com.raven.component.EventPagination;
import com.raven.entity.ChatLieu;
import com.raven.entity.Hang;
import com.raven.model.AddressDetails;
import com.raven.model.HeaderDetails;
import com.raven.model.Product;
import com.raven.model.ProductTableHeader;
import com.raven.service.ChatLieuService;
import com.raven.service.CodingErrorPdfInvoiceCreator;
import com.raven.service.HangService;
import com.raven.service.HoaDonChiTietService;
import com.raven.service.HoaDonService;
import com.raven.service.KhuyenMaiService;
import com.raven.service.MauSacService;
import com.raven.service.NguoiDungService;
import com.raven.service.SanPhamChiTietService;
import com.raven.service.SanPhamService;
import com.raven.service.SizeService;
import com.raven.style.PaginationItemRenderStyle1;
import com.raven.entity.HoaDon;
import com.raven.entity.HoaDonChiTiet;
import com.raven.entity.MauSac;
import com.raven.entity.NguoiDung;
import com.raven.entity.SanPham;
import com.raven.entity.SanPhamChiTiet;
import com.raven.entity.Size;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author RAVEN
 */
public class Form_2 extends javax.swing.JPanel {

    /**
     * Creates new form Form_1
     */
    private SanPhamChiTietService sanPhamChiTietService = new SanPhamChiTietService();
    private HangService hangService = new HangService();
    private MauSacService mauSacService = new MauSacService();
    private ChatLieuService chatLieuService = new ChatLieuService();
    private SizeService sizeService = new SizeService();
    private SanPhamService sanPhamService = new SanPhamService();
    private HoaDonService hoaDonService = new HoaDonService();
    private NguoiDungService nguoiDungService = new NguoiDungService();
    private HoaDonChiTietService hoaDonChiTietService = new HoaDonChiTietService();

    List<HoaDon> listHoaDon = new ArrayList<>();
    int INDEX_SELECT_PAGE_HD = 1;
    int CONFIG_LIMIT_DATA_PAGE = 5;

    public Form_2() {
        initComponents();
        loadComboBoxLocTrangThai();
        loadDataHoaDonToBang();
        System.out.println("OKL");
        pagination1.setPaginationItemRender(new PaginationItemRenderStyle1());
        pagination1.addEventPagination(new EventPagination() {
            @Override
            public void pageChanged(int page) {
                INDEX_SELECT_PAGE_HD = page;
                loadDataHoaDonToBang();
            }
        });
    }

    private void loadHoaDonChiTiet() {
        int maHoaDon = Integer.valueOf(tblHoaDon.getValueAt(tblHoaDon.getSelectedRow(), 0).toString());
        HoaDon hoaDon = hoaDonService.findById(maHoaDon);
        List<HoaDonChiTiet> listHoaDonChiTiet = hoaDonChiTietService.selectAllByMaHoaDon(maHoaDon);

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"#", "Mã Sản Phẩm Chi Tiết", "Tên Sản Phẩm", "Hãng", "Màu Sắc", "Size", "Chất Liệu", "Số Lượng", "Giá", "Thành tiền"});
        int index = 0;
        for (HoaDonChiTiet hoaDonChiTiet : listHoaDonChiTiet) {
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietService.findById(hoaDonChiTiet.getMaSanPhamChiTiet());
            index += 1;
            String maSanPhamChiTiet = String.valueOf(sanPhamChiTiet.getMaSanPhamChiTiet());
            String tenSanPham = String.valueOf(sanPhamService.findById(sanPhamChiTiet.getMaSanPham()).toString());
            String tenHang = hangService.findById(sanPhamChiTiet.getMaHang()).getTenHang();
            String tenMauSac = mauSacService.findById(sanPhamChiTiet.getMaMauSac()).getTenMauSac();
            String tenSize = sizeService.findById(sanPhamChiTiet.getMaSize()).getTenSize();
            String tenChatLieu = chatLieuService.findById(sanPhamChiTiet.getMaChatLieu()).getTenChatLieu();
            String thanhTien = String.valueOf(hoaDonChiTiet.getSoLuong() * sanPhamChiTiet.getGiaSanPham());
            model.addRow(new Object[]{index, maSanPhamChiTiet, tenSanPham, tenHang, tenMauSac, tenSize, tenChatLieu, hoaDonChiTiet.getSoLuong(), sanPhamChiTiet.getGiaSanPham(), thanhTien});
        }
        tblHoaDonChiTiet.setModel(model);
    }

    private void openHoaDon(String path) {
        // Đường dẫn đến tệp PDF
        String pdfPath = path;
        try {
            // Mở tệp PDF bằng lệnh dòng lệnh
            ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", "start", "", pdfPath);
            processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String xuatHoaDon() {
        int index = tblHoaDon.getSelectedRow();
        System.out.println("LIST HOA DON: " + listHoaDon);
        System.out.println("INDEX: " + index);
        if (index >= 0) {
            HoaDon hoaDon = listHoaDon.get(index);
            System.out.println(hoaDon);
            List<HoaDonChiTiet> listHoaDonChiTietDaMua = hoaDonChiTietService.selectAllByMaHoaDon(hoaDon.getMaHoaDon());
            try {
                LocalDate ld = LocalDate.now();
                String pdfName = ld + ".pdf";
                CodingErrorPdfInvoiceCreator cepdf = new CodingErrorPdfInvoiceCreator(pdfName);
                cepdf.createDocument();
                //Create Header start
                HeaderDetails header = new HeaderDetails();
                header.setInvoiceNo("RK35623").setInvoiceDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).build();
                cepdf.createHeader(header);
                //Header End
                //Create Address start
                AddressDetails addressDetails = new AddressDetails();
                addressDetails
                        .setBillingCompany("Honie Sneaker")
                        .setBillingName("ABCD")
                        .setBillingAddress("Cao dang FPT, Trinh Van Bo")
                        .setBillingEmail("honiesneakervn@gmail.com")
                        .setShippingName("Honie Sneaker \n")
                        .setShippingAddress("")
                        .build();
                cepdf.createAddress(addressDetails);
                //Address end

                //Product Start
                ProductTableHeader productTableHeader = new ProductTableHeader();
                cepdf.createTableHeader(productTableHeader);

                List<Product> productList = new ArrayList<>();
                for (HoaDonChiTiet hoaDonChiTiet : listHoaDonChiTietDaMua) {
                    SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietService.findById(hoaDonChiTiet.getMaSanPhamChiTiet());
                    MauSac mauSac = mauSacService.findById(sanPhamChiTiet.getMaMauSac());
                    ChatLieu chatLieu = chatLieuService.findById(sanPhamChiTiet.getMaChatLieu());
                    Size size = sizeService.findById(sanPhamChiTiet.getMaSize());
                    Hang hang = hangService.findById(sanPhamChiTiet.getMaHang());
                    SanPham sanPham = sanPhamService.findById(sanPhamChiTiet.getMaSanPham());
                    float thanhTien = Float.valueOf(hoaDonChiTiet.getSoLuong() * (float) sanPhamChiTiet.getGiaSanPham());
                    String moTa = String.format("Hang: %s, Mau Sac: %s, Size: %s, ChatLieu: %s", hang.getTenHang(), chatLieu.getTenChatLieu(), size.getTenSize(), mauSac.getTenMauSac());
                    productList.add(new Product(sanPham.getTenSanPham(), hang.getTenHang(), size.getTenSize(), chatLieu.getTenChatLieu(), mauSac.getTenMauSac(), hoaDonChiTiet.getSoLuong(), thanhTien));
                }
                System.out.println("LIST PRODUCTS : " + productList);
//                productList = cepdf.modifyProductList(productList);
                float tienGiamGia = hoaDon.getTongTienGiamGia();
                cepdf.createProduct(productList, String.valueOf(tienGiamGia));
                //Product End

                //Term and Condition Start
                List<String> TncList = new ArrayList<>();
//                TncList.add("1. Bên bán điện sẽ không chịu trách nhiệm trực tiếp hoặc gián tiếp với Bên mua về bất kỳ tổn thất hoặc thiệt hại nào mà Bên mua phải chịu.");
//                TncList.add("2. Người bán bảo hành sản phẩm trong 6 tháng kể từ ngày giao hàng");
                String imagePath = "src/ce_logo_circle_transparent.png";
                cepdf.createTnc(TncList, false, imagePath);
                // Term and condition end
                openHoaDon(pdfName);
                return ("Xuất hóa đơn thành công");
            } catch (Exception e) {
                System.out.println(e);
                return ("Xuất hóa đơn thất bại");
            }
        } else {
            return ("Vui lòng chọn hóa đơn");
        }

    }

    private String toDate(Date inputDate) {
        // Định dạng của chuỗi ngày tháng đầu vào
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

        // Định dạng của chuỗi ngày tháng đầu ra
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Chuyển đổi chuỗi ngày tháng thành đối tượng Date
        // Chuyển đối tượng Date thành chuỗi theo định dạng mong muốn
        String outputDateString = outputDateFormat.format(inputDate);
        return outputDateString;

    }

    private void loadDataHoaDonToBang() {
        int totalItem = 0;
        String baseSql = "SELECT * FROM HoaDon JOIN NguoiDung AS KhachHang ON HoaDon.maKhachHang = KhachHang.maNguoiDung JOIN NguoiDung AS NguoiBan ON HoaDon.maNguoiBan = NguoiBan.maNguoiDung";
        String searchText = txtSearch.getText().toLowerCase();
        // Chọn lọc hoặc tìm kiếm
        if (cboTrangThai.getSelectedIndex() != 0 || !searchText.isEmpty() || jDateNgayTao.getDate() != null || jDateNgayThanhToan.getDate() != null) {
            int selectIndex = cboTrangThai.getSelectedIndex();
            if (selectIndex != 0) {
                if (selectIndex == 1) {
                    baseSql += " WHERE trangThaiThanhToan = 1";
                }
                if (selectIndex == 2) {
                    baseSql += " WHERE trangThaiThanhToan = 2";
                }
                if (selectIndex == 3) {
                    baseSql += " WHERE trangThaiThanhToan = 3";
                }
            }
            if (jDateNgayTao.getDate() != null) {
                if (baseSql.contains("WHERE") == false) {
                    baseSql += " WHERE";
                } else {
                    baseSql += " AND";
                }
                Date ngayTao = jDateNgayTao.getDate();
                String ngay = toDate(ngayTao);
                System.out.println("NGAY: " + ngay);
                baseSql += String.format(" THOIGIANTAO = '%s'", ngay);
            }
            if (jDateNgayThanhToan.getDate() != null) {
                if (baseSql.contains("WHERE") == false) {
                    baseSql += " WHERE";
                } else {
                    baseSql += " AND";
                }
                Date ngayTao = jDateNgayThanhToan.getDate();
                String ngay = toDate(ngayTao);
                System.out.println("NGAY: " + ngay);
                baseSql += String.format(" THOIGIANTHANHTOAN = '%s'", ngay);
            }
            if (!searchText.isEmpty()) {
                if (baseSql.contains("WHERE") == false) {
                    baseSql += " WHERE";
                } else {
                    baseSql += " AND";
                }
                baseSql += " ( MAHOADON LIKE N'%" + searchText + "%' "
                        + "OR MAKHACHHANG LIKE N'%" + searchText + "%' "
                        + "OR KhachHang.tenNguoiDung LIKE N'%" + searchText + "%' "
                        + "OR MANGUOIBAN LIKE N'%" + searchText + "%' "
                        + "OR NguoiBan.tenNguoiDung LIKE N'%" + searchText + "%') ";

            }

            totalItem = hoaDonService.selectAllByCustomSql(baseSql).size();
        } else {
            totalItem = hoaDonService.selectAllByCustomSql(baseSql).size();
        }
        baseSql += String.format(" ORDER BY MAHOADON DESC OFFSET %s ROWS FETCH NEXT %s ROWS ONLY ;", CONFIG_LIMIT_DATA_PAGE * (INDEX_SELECT_PAGE_HD - 1), CONFIG_LIMIT_DATA_PAGE);
        System.out.println("BASE SQL: " + baseSql);
        listHoaDon = hoaDonService.selectAllByCustomSql(baseSql);

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Mã hóa đơn", "Mã khách hàng", "Tên khách hàng", "Tên người bán", "Tiền trước giảm", "Tiền giảm", "Thanh toán", "Ngày tạo", "Ngày thanh toán", "Trạng thái"});
        for (HoaDon hoaDon : listHoaDon) {
            NguoiDung nguoiMua = nguoiDungService.findById(hoaDon.getMaKhachHang());
            NguoiDung nguoiBan = nguoiDungService.findById(hoaDon.getMaNguoiBan());
            String trangThaiHoaDon = "Chưa thanh toán";
            if (hoaDon.getTrangThaiThanhToan() == 1) {
                trangThaiHoaDon = "Chờ thanh toán";
            } else if (hoaDon.getTrangThaiThanhToan() == 2) {
                trangThaiHoaDon = "Đã thanh toán";
            } else if (hoaDon.getTrangThaiThanhToan() == 3) {
                trangThaiHoaDon = "Hủy thanh toán";
            }
            model.addRow(new Object[]{hoaDon.getMaHoaDon(), hoaDon.getMaKhachHang(), nguoiMua.getTenNguoiDung(), nguoiBan.getTenNguoiDung(), hoaDon.getTongTienTruocGiamGia(), hoaDon.getTongTienGiamGia(), hoaDon.getTongTienSauGiamGia(), hoaDon.getThoiGianTao(), hoaDon.getThoiGianThanhToan(), trangThaiHoaDon});
        }
        tblHoaDon.setModel(model);
        pagination1.setPagegination(INDEX_SELECT_PAGE_HD, (int) Math.ceil((float) totalItem / CONFIG_LIMIT_DATA_PAGE));
    }

    private void loadComboBoxLocTrangThai() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement("Tất cả");
        model.addElement("Chờ thanh toán");
        model.addElement("Đã thành toán");
        model.addElement("Hủy thanh toán");
        cboTrangThai.setModel(model);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHoaDon = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        pagination1 = new com.raven.component.Pagination();
        jButton1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblHoaDonChiTiet = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        cboTrangThai = new javax.swing.JComboBox<>();
        jPanel8 = new javax.swing.JPanel();
        jDateNgayTao = new com.toedter.calendar.JDateChooser();
        jButton2 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jDateNgayThanhToan = new com.toedter.calendar.JDateChooser();
        jButton3 = new javax.swing.JButton();

        setBackground(new java.awt.Color(242, 242, 242));

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
                .addComponent(txtSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 9, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Hóa đơn"));

        tblHoaDon.setModel(new javax.swing.table.DefaultTableModel(
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
        tblHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblHoaDonMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblHoaDon);

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));

        pagination1.setBackground(new java.awt.Color(102, 102, 102));
        pagination1.setOpaque(false);
        jPanel2.add(pagination1);

        jButton1.setText("Xuất hóa đơn");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Hóa đơn chi tiết"));

        tblHoaDonChiTiet.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tblHoaDonChiTiet);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 20, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Trạng thái"));

        cboTrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboTrangThai.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboTrangThaiItemStateChanged(evt);
            }
        });
        cboTrangThai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cboTrangThaiMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cboTrangThai, javax.swing.GroupLayout.Alignment.TRAILING, 0, 151, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(cboTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Ngày tạo"));

        jDateNgayTao.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateNgayTaoPropertyChange(evt);
            }
        });

        jButton2.setText("X");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jDateNgayTao, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton2)
                    .addComponent(jDateNgayTao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Ngày thanh toán"));

        jDateNgayThanhToan.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateNgayThanhToanPropertyChange(evt);
            }
        });

        jButton3.setText("X");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jDateNgayThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(7, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3)
                    .addComponent(jDateNgayThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        loadDataHoaDonToBang();
    }//GEN-LAST:event_txtSearchKeyReleased

    private void cboTrangThaiItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboTrangThaiItemStateChanged
        // TODO add your handling code here:
        INDEX_SELECT_PAGE_HD = 1;
        loadDataHoaDonToBang();
    }//GEN-LAST:event_cboTrangThaiItemStateChanged

    private void cboTrangThaiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboTrangThaiMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cboTrangThaiMouseClicked

    private void tblHoaDonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonMousePressed
        // TODO add your handling code here:
        loadHoaDonChiTiet();
    }//GEN-LAST:event_tblHoaDonMousePressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(this, xuatHoaDon());
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jDateNgayTaoPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateNgayTaoPropertyChange
        // TODO add your handling code here:
        if ("date".equals(evt.getPropertyName())) {
            System.out.println(evt.getPropertyName()
                    + ": " + (Date) evt.getNewValue());
            loadDataHoaDonToBang();
        }
    }//GEN-LAST:event_jDateNgayTaoPropertyChange

    private void jDateNgayThanhToanPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateNgayThanhToanPropertyChange
        // TODO add your handling code here:
        if ("date".equals(evt.getPropertyName())) {
            System.out.println(evt.getPropertyName()
                    + ": " + (Date) evt.getNewValue());
            loadDataHoaDonToBang();
        }

    }//GEN-LAST:event_jDateNgayThanhToanPropertyChange

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:

        jDateNgayTao.setDate(null);
        loadDataHoaDonToBang();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        jDateNgayThanhToan.setDate(null);
        loadDataHoaDonToBang();
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cboTrangThai;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private com.toedter.calendar.JDateChooser jDateNgayTao;
    private com.toedter.calendar.JDateChooser jDateNgayThanhToan;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private com.raven.component.Pagination pagination1;
    private javax.swing.JTable tblHoaDon;
    private javax.swing.JTable tblHoaDonChiTiet;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
