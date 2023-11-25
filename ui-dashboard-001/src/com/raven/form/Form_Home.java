package com.raven.form;

import com.raven.model.Model_Card;
import com.raven.model.StatusType;
import com.raven.service.ChatLieuService;
import com.raven.service.HangService;
import com.raven.service.HoaDonChiTietService;
import com.raven.service.HoaDonService;
import com.raven.service.MauSacService;
import com.raven.service.NguoiDungService;
import com.raven.service.SanPhamChiTietService;
import com.raven.service.SanPhamService;
import com.raven.service.SizeService;
import com.raven.swing.ScrollBar;
import com.raven.utils.Auth;
import com.ravent.entity.HoaDon;
import com.ravent.entity.HoaDonChiTiet;
import com.ravent.entity.NguoiDung;
import com.ravent.entity.SanPham;
import com.ravent.entity.SanPhamChiTiet;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

public class Form_Home extends javax.swing.JPanel {

    private SanPhamChiTietService sanPhamChiTietService = new SanPhamChiTietService();
    private HangService hangService = new HangService();
    private MauSacService mauSacService = new MauSacService();
    private ChatLieuService chatLieuService = new ChatLieuService();
    private SizeService sizeService = new SizeService();
    private SanPhamService sanPhamService = new SanPhamService();
    private HoaDonService hoaDonService = new HoaDonService();
    private NguoiDungService nguoiDungService = new NguoiDungService();
    private HoaDonChiTietService hoaDonChiTietService = new HoaDonChiTietService();

    List<SanPhamChiTiet> listSanPhamChiTiet = new ArrayList<>();
    List<HoaDon> listHoaDon = new ArrayList<>();
    List<HoaDonChiTiet> listHoaDonChiTiet = new ArrayList<>();
    int INDEX_ROW_SELECTED_SPCT = 0;

    int GLOBAL_MA_KHACH_HANG = 0;
    int GLOBAL_MA_HOA_DON = 0;

    public Form_Home() {
        initComponents();
        init_loadSanPhamToComboBox();
        setPopupMenuTbl();

        loadSanPhamChiTietToTable();
        loadHoaDonChoThanhToan();
        loadHoaDonChiTiet();

        System.out.println("LOGIN: " + new Auth().user.getTenNguoiDung());

    }

    private void openDialogSelectKhachHang() {
        DialogSelectKhachHang dialogSelectKhachHang = new DialogSelectKhachHang(new javax.swing.JFrame(), true);
        dialogSelectKhachHang.setVisible(true);
        dialogSelectKhachHang.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("Dialog đã đóng");
                loadHoaDonChoThanhToan();
            }
        });

    }

    private void calledHoaDon() {
        int choice = JOptionPane.showConfirmDialog(this, "Có muốn hủy hóa đơn này không?");
        if (choice == 0) {
            // Cập nhật lại số lượng sau hủy
            HoaDon hoaDon = listHoaDon.get(tblHoaDonCho.getSelectedRow());
            List<HoaDonChiTiet> listHoaDonChiTiet = hoaDonChiTietService.selectAllByMaHoaDon(hoaDon.getMaHoaDon());
            for (HoaDonChiTiet hoaDonChiTiet : listHoaDonChiTiet) {
                System.out.println(hoaDonChiTiet);
                SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietService.findById(hoaDonChiTiet.getMaSanPhamChiTiet());
                sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() + hoaDonChiTiet.getSoLuong());
                String statusHuyHoaDon = sanPhamChiTietService.update(sanPhamChiTiet);
                System.out.println("STATUS TRU SO LUONG DON: " + statusHuyHoaDon);
                loadSanPhamChiTietToTable();
            }
            // Chuyển trạng thái hóa đơn sang hủy MÃ: 3
            hoaDon.setTrangThaiThanhToan(3);
            hoaDon.setThoiGianThanhToan(new Date());
            System.out.println(hoaDon);
            String statusUpdateHoaDon = hoaDonService.called(hoaDon);
            System.out.println("STATUS UPDATE DON: " + statusUpdateHoaDon);
            showMessageBox(statusUpdateHoaDon);
            loadHoaDonChoThanhToan();
            // Xóa hết dữ liệu giỏ hàng nêú ko còn hóa đơn chờ
            listHoaDon = hoaDonService.selectAllByMaTrangThaiThanhToan(2);
            if (listHoaDon.size() == 0) {
                System.out.println("Hết Hóa ĐƠn chờ");
                GLOBAL_MA_HOA_DON = 0;
                loadHoaDonChiTiet();
            }
        }
    }

    private void setPopupMenuTbl() {
        // Create a popup menu
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBackground(Color.BLACK);
        JMenuItem jMenuThemVaoGioHang = new JMenuItem("Thêm vào giỏ hàng");
        jMenuThemVaoGioHang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (GLOBAL_MA_HOA_DON == 0) {
                    showMessageBox("Vui Lòng chọn hóa đơn chờ");
                    return;
                } else {
                    System.out.println("Call Them Vao Gio Hang");
                    callInputDialogSoLuong();

                }

            }
        });
        popupMenu.add(jMenuThemVaoGioHang);
        // Attach the popup menu to the JTable
        tblSanPhamChiTiet.setComponentPopupMenu(popupMenu);
    }

    private void callInputDialogSoLuong() {
        SanPhamChiTiet sanPhamChiTiet = listSanPhamChiTiet.get(INDEX_ROW_SELECTED_SPCT);
        String inputText = JOptionPane.showInputDialog(String.format("Vui lòng nhập số lượng sản phẩm có mã [%s]", sanPhamChiTiet.getMaSanPhamChiTiet()));
        System.out.println(inputText);
        if (inputText != null) {
            if (!inputText.matches("\\d+")) {
                showMessageBox("Số lượng nhập vào phải là số");
                return;
            }
            if (Integer.valueOf(inputText) <= 0) {
                showMessageBox("Số lượng nhập vào phải là số lớn hơn 0");
                return;
            } else {
                if (Integer.valueOf(inputText) > sanPhamChiTiet.getSoLuong()) {
                    showMessageBox("Số lượng nhập vào quá với số lượng đang có");
                    return;
                } else {
                    int soLuong = Integer.valueOf(inputText);
                    SanPhamChiTiet sanPhamChiTietSeleted = listSanPhamChiTiet.get(INDEX_ROW_SELECTED_SPCT);

                    // Lẫy danh sách ma San Pham Trong Hoa Don Hien Tai
                    List<Integer> listMaSanPhamTrongHDCT = new ArrayList<>();
                    List<HoaDonChiTiet> list = hoaDonChiTietService.selectAllByMaHoaDon(GLOBAL_MA_HOA_DON);
                    for (HoaDonChiTiet hoaDonChiTiet : list) {
                        listMaSanPhamTrongHDCT.add(hoaDonChiTiet.getMaSanPhamChiTiet());
                    }
                    //
                    HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet(0, sanPhamChiTietSeleted.getMaSanPhamChiTiet(), GLOBAL_MA_HOA_DON, soLuong);
                    if (listMaSanPhamTrongHDCT.contains(sanPhamChiTietSeleted.getMaSanPhamChiTiet())) {
                        // Cập nhật số lượng
                        System.out.println("Sản phẩm đã tồn tại trong giỏ hàng");
                        int maSPCT = sanPhamChiTietSeleted.getMaSanPham();
                        int maHoaDon = GLOBAL_MA_HOA_DON;
                        HoaDonChiTiet hoaDonChiTietSelect = hoaDonChiTietService.findBy_MaHoaDon_MaSPCT(maHoaDon, maSPCT);
                        hoaDonChiTiet.setMaHoaDonChiTiet(hoaDonChiTietSelect.getMaHoaDonChiTiet());
                        hoaDonChiTiet.setSoLuong(soLuong + hoaDonChiTietSelect.getSoLuong());
                        String statusUpdateHoaDon = hoaDonChiTietService.update(hoaDonChiTiet);
                        System.out.println("STATUS SUA SO LUONG TRONG HOA DON: " + statusUpdateHoaDon);
                        loadHoaDonChiTiet();

                        // Trừ đi số lượng sản phẩm chi tiết đang có
                        sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - soLuong);
                        System.out.println(sanPhamChiTiet);
                        String statusSuaSoLuong = sanPhamChiTietService.update(sanPhamChiTiet);
                        System.out.println("STATUS SUA SO LUONG TRONG SPCT: " + statusSuaSoLuong);
                        loadSanPhamChiTietToTable();

                    } else {
                        // Thêm mới
                        hoaDonChiTietService.add(hoaDonChiTiet);
                        loadHoaDonChiTiet();

                        // Trừ đi số lượng sản phẩm chi tiết đang có
                        sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - soLuong);
                        System.out.println(sanPhamChiTiet);
                        String statusSuaSoLuong = sanPhamChiTietService.update(sanPhamChiTiet);
                        System.out.println("STATUS SUA SO LUONG TRONG SPCT: " + statusSuaSoLuong);
                        loadSanPhamChiTietToTable();

                    }

                }
            }
        }

    }

    public void showMessageBox(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void loadHoaDonChiTiet() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Mã sản phẩm", "Tên sản phẩm", "Đơn giá", "Số lượng", "Thành tiền"});
        listHoaDonChiTiet.clear();
        if (GLOBAL_MA_HOA_DON != 0) {
            listHoaDonChiTiet = hoaDonChiTietService.selectAllByMaHoaDon(GLOBAL_MA_HOA_DON);
            for (HoaDonChiTiet hoaDonChiTiet : listHoaDonChiTiet) {
                SanPham sanPham = sanPhamService.findById(sanPhamChiTietService.findById(hoaDonChiTiet.getMaSanPhamChiTiet()).getMaSanPham());
                String tenSanPham = sanPham.getTenSanPham();
                String donGia = String.valueOf(sanPhamChiTietService.findById(hoaDonChiTiet.getMaSanPhamChiTiet()).getGiaSanPham());
                model.addRow(new Object[]{hoaDonChiTiet.getMaSanPhamChiTiet(), tenSanPham, donGia, hoaDonChiTiet.getSoLuong(), String.valueOf(Float.valueOf(donGia) * hoaDonChiTiet.getSoLuong())});
            }
        }
        tblGioHang.setModel(model);
    }

    public void loadHoaDonChoThanhToan() {
        listHoaDon = hoaDonService.selectAllByMaTrangThaiThanhToan(2);
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Mã hóa đơn", "Người Bán", "Tên khách hàng"});
        for (HoaDon hoaDon : listHoaDon) {
            String tenNguoiBan = nguoiDungService.findById(hoaDon.getMaNguoiBan()).getTenTaiKhoan();
            String tenKhachHang = nguoiDungService.findById(hoaDon.getMaKhachHang()).getTenNguoiDung();
            model.addRow(new Object[]{hoaDon.getMaHoaDon(), tenNguoiBan, tenKhachHang});
        }
        tblHoaDonCho.setModel(model);
    }

    public void loadSanPhamChiTietToTable() {
        String searchText = jTextField1.getText().toLowerCase();
        String baseSql = "";
        if (!searchText.isEmpty()) {
            baseSql = "SELECT * FROM [SanPhamChiTiet] "
                    + "JOIN SanPham ON SanPhamChiTiet.maSanPham = SanPham.maSanPham "
                    + "JOIN HANG ON SanPhamChiTiet.maHang = Hang.maHang "
                    + "JOIN MauSac ON SanPhamChiTiet.maMauSac = MauSac.maMauSac "
                    + "JOIN Size ON SanPhamChiTiet.maSize = Size.maSize "
                    + "JOIN ChatLieu ON SanPhamChiTiet.maChatLieu = ChatLieu.maChatLieu "
                    + "WHERE ( tenChatLieu LIKE N'%" + searchText + "%' "
                    + "OR tenSize LIKE N'%" + searchText + "%' "
                    + "OR tenHang LIKE N'%" + searchText + "%' "
                    + "OR tenMauSac LIKE N'%" + searchText + "%') ";
            if (cboSanPham.getSelectedItem() != null) {
                SanPham sanPhamSelected = ((SanPham) cboSanPham.getSelectedItem());
                baseSql += String.format(" AND SanPhamChiTiet.MASANPHAM = %s", sanPhamSelected.getMaSanPham());
            }
        } else {
            baseSql = "SELECT * FROM [SanPhamChiTiet] JOIN SanPham ON SanPhamChiTiet.maSanPham = SanPham.maSanPham JOIN HANG ON SanPhamChiTiet.maHang = Hang.maHang JOIN MauSac ON SanPhamChiTiet.maMauSac = MauSac.maMauSac JOIN Size ON SanPhamChiTiet.maSize = Size.maSize JOIN ChatLieu ON SanPhamChiTiet.maChatLieu = ChatLieu.maChatLieu";
            if (cboSanPham.getSelectedItem() != null) {
                SanPham sanPhamSelected = ((SanPham) cboSanPham.getSelectedItem());
                baseSql += String.format(" WHERE SanPhamChiTiet.MASANPHAM = %s", sanPhamSelected.getMaSanPham());
            }
        }

//        System.out.println(baseSql);
        listSanPhamChiTiet = sanPhamChiTietService.selectAllByCustomSql(baseSql);
        addRowToTblSanPhamChiTiet(listSanPhamChiTiet);
    }

    public void addRowToTblSanPhamChiTiet(List<SanPhamChiTiet> listSanPhamChiTiet) {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Stt", "Mã Sản Phẩm Chi Tiết", "Tên Sản Phẩm", "Hãng", "Màu Sắc", "Size", "Chất Liệu", "Số Lượng", "Giá"});
        int index = 0;
        for (SanPhamChiTiet sanPhamChiTiet : listSanPhamChiTiet) {
            index += 1;
            String maSanPhamChiTiet = String.valueOf(sanPhamChiTiet.getMaSanPhamChiTiet());
            String tenSanPham = String.valueOf(sanPhamService.findById(sanPhamChiTiet.getMaSanPham()).toString());
            String tenHang = hangService.findById(sanPhamChiTiet.getMaHang()).getTenHang();
            String tenMauSac = mauSacService.findById(sanPhamChiTiet.getMaMauSac()).getTenMauSac();
            String tenSize = sizeService.findById(sanPhamChiTiet.getMaSize()).getTenSize();
            String tenChatLieu = chatLieuService.findById(sanPhamChiTiet.getMaChatLieu()).getTenChatLieu();
            model.addRow(new Object[]{index, maSanPhamChiTiet, tenSanPham, tenHang, tenMauSac, tenSize, tenChatLieu, sanPhamChiTiet.getSoLuong(), sanPhamChiTiet.getGiaSanPham()});
        }
        tblSanPhamChiTiet.setModel(model);

    }

    public void init_loadSanPhamToComboBox() {
        List<SanPham> listSanPham = sanPhamService.selectAll();
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement(null);
        for (SanPham sanPham : listSanPham) {
            model.addElement(sanPham);
        }
        cboSanPham.setModel(model);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lbMaHoaDon = new javax.swing.JLabel();
        lbTongTienTruoc = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lbMaGiamGia = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lbTenKhachHang = new javax.swing.JLabel();
        lbTongGiamGia = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        lbTongThanhToan = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblHoaDonCho = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblSanPhamChiTiet = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        cboSanPham = new javax.swing.JComboBox<>();
        jPanel6 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblGioHang = new javax.swing.JTable();

        setBackground(new java.awt.Color(242, 242, 242));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Hóa đơn chờ"));

        jButton3.setText("Thanh Toan");

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin hóa đơn"));

        jLabel10.setText("Mã hóa đơn");

        jLabel11.setText("Mã giảm giá");

        lbMaHoaDon.setText("ABCDR");

        lbTongTienTruoc.setText("ABCDR");

        jLabel12.setText("Tổng tiền trước");

        lbMaGiamGia.setText("ABCDR");

        jLabel15.setText("Tên khách hàng");

        lbTenKhachHang.setText("ABCDR");

        lbTongGiamGia.setText("ABCDR");

        jLabel26.setText("Giảm giá");

        jLabel28.setText("Tổng thanh toán");

        lbTongThanhToan.setText("ABCDR");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbMaHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                    .addComponent(lbTongTienTruoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbMaGiamGia, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbTenKhachHang, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbTongGiamGia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbTongThanhToan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbMaHoaDon))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbMaGiamGia))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbTenKhachHang))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbTongTienTruoc))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbTongGiamGia)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbTongThanhToan))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton4.setText("Tạo hóa đơn");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Hủy hóa đơn");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        tblHoaDonCho.setModel(new javax.swing.table.DefaultTableModel(
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
        tblHoaDonCho.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblHoaDonChoMousePressed(evt);
            }
        });
        jScrollPane2.setViewportView(tblHoaDonCho);

        jButton1.setText("Áp mã");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addGap(2, 2, 2)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton1))
                .addContainerGap())
        );

        jPanel4.getAccessibleContext().setAccessibleName("Thông tin hóa đơn");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Danh sách sản phẩm"));

        tblSanPhamChiTiet.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8"
            }
        ));
        tblSanPhamChiTiet.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblSanPhamChiTiet.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblSanPhamChiTiet.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblSanPhamChiTietMousePressed(evt);
            }
        });
        jScrollPane3.setViewportView(tblSanPhamChiTiet);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Sản phẩm"));

        cboSanPham.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboSanPham.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboSanPhamItemStateChanged(evt);
            }
        });
        cboSanPham.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cboSanPhamKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cboSanPham, 0, 206, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(cboSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Tìm kiếm"));

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTextField1)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Giỏ hàng"));

        tblGioHang.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblGioHang);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tblSanPhamChiTietMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSanPhamChiTietMousePressed
        // TODO add your handling code here:
        INDEX_ROW_SELECTED_SPCT = tblSanPhamChiTiet.getSelectedRow();
        System.out.println("SELECT ROW :" + INDEX_ROW_SELECTED_SPCT);
    }//GEN-LAST:event_tblSanPhamChiTietMousePressed

    private void cboSanPhamKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboSanPhamKeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_cboSanPhamKeyReleased

    private void cboSanPhamItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboSanPhamItemStateChanged
        // TODO add your handling code here:
        loadSanPhamChiTietToTable();
    }//GEN-LAST:event_cboSanPhamItemStateChanged

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        // TODO add your handling code here:
        loadSanPhamChiTietToTable();
    }//GEN-LAST:event_jTextField1KeyReleased

    private void tblHoaDonChoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonChoMousePressed
        // TODO add your handling code here:
        GLOBAL_MA_HOA_DON = Integer.valueOf(tblHoaDonCho.getValueAt(tblHoaDonCho.getSelectedRow(), 0).toString());
        HoaDon hoaDon = listHoaDon.get(tblHoaDonCho.getSelectedRow());
        lbMaHoaDon.setText(String.valueOf(hoaDon.getMaHoaDon()));
        lbMaGiamGia.setText(hoaDon.getMaGiamGia());
        lbTongTienTruoc.setText(String.valueOf(hoaDon.getTongTienTruocGiamGia()));
        lbTongGiamGia.setText(String.valueOf(hoaDon.getTongTienGiamGia()));
        lbTongThanhToan.setText(String.valueOf(hoaDon.getTongTienSauGiamGia()));
        loadHoaDonChiTiet();
    }//GEN-LAST:event_tblHoaDonChoMousePressed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        openDialogSelectKhachHang();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        calledHoaDon();
    }//GEN-LAST:event_jButton5ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cboSanPham;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lbMaGiamGia;
    private javax.swing.JLabel lbMaHoaDon;
    private javax.swing.JLabel lbTenKhachHang;
    private javax.swing.JLabel lbTongGiamGia;
    private javax.swing.JLabel lbTongThanhToan;
    private javax.swing.JLabel lbTongTienTruoc;
    private javax.swing.JTable tblGioHang;
    private javax.swing.JTable tblHoaDonCho;
    private javax.swing.JTable tblSanPhamChiTiet;
    // End of variables declaration//GEN-END:variables
}
