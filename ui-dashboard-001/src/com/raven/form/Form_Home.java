package com.raven.form;

import com.raven.component.EventPagination;
import com.raven.model.Model_Card;
import com.raven.model.StatusType;
import com.raven.service.ChatLieuService;
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
import com.raven.swing.ScrollBar;
import com.raven.utils.Auth;
import com.raven.entity.ChatLieu;
import com.raven.entity.Hang;
import com.raven.entity.HoaDon;
import com.raven.entity.HoaDonChiTiet;
import com.raven.entity.KhuyenMai;
import com.raven.entity.MauSac;
import com.raven.entity.NguoiDung;
import com.raven.entity.SanPham;
import com.raven.entity.SanPhamChiTiet;
import com.raven.entity.Size;

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
import javax.swing.JComboBox;
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
    private KhuyenMaiService khuyenMaiService = new KhuyenMaiService();

    List<SanPhamChiTiet> listSanPhamChiTiet = new ArrayList<>();
    List<HoaDon> listHoaDon = new ArrayList<>();
    List<HoaDonChiTiet> listHoaDonChiTiet = new ArrayList<>();
    int INDEX_ROW_SELECTED_SPCT = -1;
    int INDEX_ROW_SELECTED_HDC = -1;
    int INDEX_ROW_SELECTED_HDCT = -1;
    
    int INDEX_SELECT_PAGE_SPCT = 1;

    int GLOBAL_MA_KHACH_HANG = 0;
    int GLOBAL_MA_HOA_DON = 0;
    int CONFIG_LIMIT_DATA_PAGE = 5;

    public Form_Home() {
        initComponents();
        init_loadSanPhamToComboBox();
        setPopupMenuTbl();
        setPopupMenuGioHang();

        loadSanPhamChiTietToTable();
        loadHoaDonChoThanhToan();
        loadHoaDonChiTietByMaHoaDon();

        pagination1.setPaginationItemRender(new PaginationItemRenderStyle1());
        pagination1.addEventPagination(new EventPagination() {
            @Override
            public void pageChanged(int page) {
                INDEX_SELECT_PAGE_SPCT = page;
                loadSanPhamChiTietToTable();
            }
        });

        System.out.println("LOGIN: " + new Auth().user.getTenNguoiDung());
    }

    public void loadHoaDonChoThanhToan() {
        listHoaDon = hoaDonService.selectAllByMaTrangThaiThanhToan(1);
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Mã hóa đơn", "Người Bán", "Tên khách hàng"});
        for (HoaDon hoaDon : listHoaDon) {
            String tenNguoiBan = nguoiDungService.findById(hoaDon.getMaNguoiBan()).getTenTaiKhoan();
            String tenKhachHang = nguoiDungService.findById(hoaDon.getMaKhachHang()).getTenNguoiDung();
            model.addRow(new Object[]{hoaDon.getMaHoaDon(), tenNguoiBan, tenKhachHang});
        }
        tblHoaDonCho.setModel(model);
    }

    public void thanhToanHoaDon() {
        HoaDon hoaDon = hoaDonService.findById(GLOBAL_MA_HOA_DON);
        if (hoaDon != null) {
            int choice = JOptionPane.showConfirmDialog(this, "Xác nhận thanh toán hóa đơn này");
            if (choice == 0) {
                hoaDon.setTrangThaiThanhToan(2);
                hoaDon.setThoiGianThanhToan(new Date());
                hoaDonService.complete(hoaDon);
                loadHoaDonChoThanhToan();
                reloadInfoHoaDonByMaHoaDon();
                // Hết hóa đơn chờ
                // Xóa hết dữ liệu giỏ hàng nêú ko còn hóa đơn chờ
                listHoaDon = hoaDonService.selectAllByMaTrangThaiThanhToan(1);
                if (listHoaDon.size() == 0) {
                    System.out.println("Hết Hóa Đơn chờ");
                    GLOBAL_MA_HOA_DON = 0;
                    loadHoaDonChiTietByMaHoaDon();
                } // Vẫn còn hóa đơn chờ
                // Reload lại về hóa đơn chờ đầu
                else {
                    INDEX_ROW_SELECTED_HDC = 0;
                    GLOBAL_MA_HOA_DON = listHoaDon.get(INDEX_ROW_SELECTED_HDC).getMaHoaDon();
                    reloadInfoHoaDonByMaHoaDon();
                    loadHoaDonChiTietByMaHoaDon();
                }
            }
        }
    }

    private void reloadInfoHoaDonByMaHoaDon() {
        HoaDon hoaDon = hoaDonService.findById(GLOBAL_MA_HOA_DON);
        if (hoaDon != null) {
            tblHoaDonCho.getSelectionModel().setSelectionInterval(INDEX_ROW_SELECTED_HDC, INDEX_ROW_SELECTED_HDC);
            List<HoaDonChiTiet> listHoaDonChiTietTrongHoaDon = hoaDonChiTietService.selectAllByMaHoaDon(hoaDon.getMaHoaDon());
            float tongTienTruocGiam = 0;
            float tongTienSauGiamGia = 0;
            float tongTienGiamGia = 0;
            System.out.println(listHoaDonChiTietTrongHoaDon);

            for (HoaDonChiTiet hoaDonChiTiet : listHoaDonChiTietTrongHoaDon) {
                SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietService.findById(hoaDonChiTiet.getMaSanPhamChiTiet());
                tongTienTruocGiam += (hoaDonChiTiet.getSoLuong() * sanPhamChiTiet.getGiaSanPham());
            }
            KhuyenMai khuyenMai = khuyenMaiService.findById(hoaDon.getMaGiamGia());
            if (khuyenMai != null) {
                tongTienGiamGia = khuyenMai.getMenhGia();
            }
            if (tongTienTruocGiam < tongTienGiamGia) {
                tongTienSauGiamGia = 0;
            } else {
                tongTienSauGiamGia = tongTienTruocGiam - tongTienGiamGia;
            }
            hoaDon.setTongTienTruocGiamGia(tongTienTruocGiam);
            hoaDon.setTongTienGiamGia(tongTienGiamGia);
            hoaDon.setTongTienSauGiamGia(tongTienSauGiamGia);
            String reloadStatus = hoaDonService.update(hoaDon);
            System.out.println("====================================");
            System.out.println("RELOAD STATUS: " + reloadStatus);
            System.out.println("TONG TIEN TRUOC: " + tongTienTruocGiam);
            System.out.println("TONG TIEN GIAM: " + tongTienGiamGia);
            System.out.println("TONG TIEN SAU : " + tongTienSauGiamGia);
            System.out.println("====================================");
            lbMaHoaDon.setText(String.valueOf(hoaDon.getMaHoaDon()));
            lbMaGiamGia.setText(hoaDon.getMaGiamGia() == null ? "Null" : hoaDon.getMaGiamGia());
            lbTongTienTruoc.setText(String.valueOf(hoaDon.getTongTienTruocGiamGia()));
            lbTongGiamGia.setText(String.valueOf(hoaDon.getTongTienGiamGia()));
            lbTongThanhToan.setText(String.valueOf(hoaDon.getTongTienSauGiamGia()));
            lbTenKhachHang.setText(nguoiDungService.findById(hoaDon.getMaKhachHang()).getTenNguoiDung());
        } else {
            lbMaHoaDon.setText("NULL");
            lbMaGiamGia.setText("NULL");
            lbTongTienTruoc.setText("NULL");
            lbTongGiamGia.setText("NULL");
            lbTongThanhToan.setText("NULL");
            lbTenKhachHang.setText("NULL");

        }
    }

    private void openDialogSelectKhuyenMai() {
        HoaDon hoaDon = hoaDonService.findById(GLOBAL_MA_HOA_DON);
        if (hoaDon != null) {
            DialogSelectKhuyenMai dialogSelectKhachHang = new DialogSelectKhuyenMai(new javax.swing.JFrame(), true, GLOBAL_MA_HOA_DON);
            dialogSelectKhachHang.setVisible(true);
            dialogSelectKhachHang.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    System.out.println("Dialog đã đóng");
                    reloadInfoHoaDonByMaHoaDon();

                }
            });
        } else {
            showMessageBox("Vui lòng chọn hóa đơn chờ");
        }

    }

    private void openDialogSelectKhachHang() {
        DialogSelectKhachHang dialogSelectKhachHang = new DialogSelectKhachHang(new javax.swing.JFrame(), true);
        dialogSelectKhachHang.setVisible(true);
        dialogSelectKhachHang.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("Dialog đã đóng");
                loadHoaDonChoThanhToan();
                INDEX_ROW_SELECTED_HDC = tblHoaDonCho.getRowCount() - 1;
                if (INDEX_ROW_SELECTED_HDC >= 0) {
                    System.out.println(INDEX_ROW_SELECTED_HDC);
                    GLOBAL_MA_HOA_DON = Integer.valueOf(tblHoaDonCho.getValueAt(INDEX_ROW_SELECTED_HDC, 0).toString());
                    reloadInfoHoaDonByMaHoaDon();
                    loadHoaDonChiTietByMaHoaDon();
                }

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
            // Hết hóa đơn chờ
            // Xóa hết dữ liệu giỏ hàng nêú ko còn hóa đơn chờ
            listHoaDon = hoaDonService.selectAllByMaTrangThaiThanhToan(2);
            if (listHoaDon.size() == 0) {
                System.out.println("Hết Hóa Đơn chờ");
                GLOBAL_MA_HOA_DON = 0;
                loadHoaDonChiTietByMaHoaDon();
            } // Vẫn còn hóa đơn chờ
            // Reload lại về hóa đơn chờ đầu
            else {
                INDEX_ROW_SELECTED_HDC = 0;
                GLOBAL_MA_HOA_DON = listHoaDon.get(INDEX_ROW_SELECTED_HDC).getMaHoaDon();
                reloadInfoHoaDonByMaHoaDon();
                loadHoaDonChiTietByMaHoaDon();

            }

        }
    }

    private void xoaHoaDonChiTietKhoiGioHang() {
        INDEX_ROW_SELECTED_HDCT = tblGioHang.getSelectedRow();
        HoaDonChiTiet hoaDonChiTietSelected = listHoaDonChiTiet.get(INDEX_ROW_SELECTED_HDCT);
        SanPhamChiTiet sanPhamChiTietSelected = sanPhamChiTietService.findById(hoaDonChiTietSelected.getMaSanPhamChiTiet());
        int choice = JOptionPane.showConfirmDialog(this, String.format("Xác nhận hủy thêm sản phầm có mã [%s] này ?", hoaDonChiTietSelected.getMaSanPhamChiTiet()));
        if (choice == 0) {
            hoaDonChiTietService.delete(hoaDonChiTietSelected.getMaHoaDonChiTiet());
            sanPhamChiTietSelected.setSoLuong(sanPhamChiTietSelected.getSoLuong() + hoaDonChiTietSelected.getSoLuong());
            showMessageBox(sanPhamChiTietService.called(sanPhamChiTietSelected));
            loadHoaDonChiTietByMaHoaDon();
            loadSanPhamChiTietToTable();
            reloadInfoHoaDonByMaHoaDon();
        }
    }

    private void updateAgainSoLuong() {
        INDEX_ROW_SELECTED_HDCT = tblGioHang.getSelectedRow();
        HoaDonChiTiet hoaDonChiTietSelected = listHoaDonChiTiet.get(INDEX_ROW_SELECTED_HDCT);
        String inputText = JOptionPane.showInputDialog(String.format("Vui lòng nhập số lượng sản phẩm có mã [%s]", hoaDonChiTietSelected.getMaSanPhamChiTiet()));
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

                int soLuong = Integer.valueOf(inputText);
                SanPhamChiTiet sanPhamChiTietSeleted = sanPhamChiTietService.findById(hoaDonChiTietSelected.getMaSanPhamChiTiet());
                // Sửa số lượng lớn hơn ban đầu
                if (soLuong >= hoaDonChiTietSelected.getSoLuong()) {
                    // Số lượng cần thêm vào hdct
                    int canThemVaoHoaDon = soLuong - hoaDonChiTietSelected.getSoLuong();
                    // đủ hàng
                    if (canThemVaoHoaDon <= sanPhamChiTietSeleted.getSoLuong()) {
                        // Cập nhật số lượng
                        hoaDonChiTietSelected.setSoLuong(canThemVaoHoaDon + hoaDonChiTietSelected.getSoLuong());
                        String statusUpdateHoaDon = hoaDonChiTietService.update(hoaDonChiTietSelected);
                        System.out.println("STATUS SUA SO LUONG TRONG HOA DON: " + statusUpdateHoaDon);
                        loadHoaDonChiTietByMaHoaDon();
                        // Trừ đi số lượng sản phẩm cần trừ
                        sanPhamChiTietSeleted.setSoLuong(sanPhamChiTietSeleted.getSoLuong() - canThemVaoHoaDon);
                        System.out.println(sanPhamChiTietSeleted);
                        String statusSuaSoLuong = sanPhamChiTietService.update(sanPhamChiTietSeleted);
                        System.out.println("STATUS SUA SO LUONG TRONG SPCT: " + statusSuaSoLuong);
                        loadSanPhamChiTietToTable();
                    } else {
                        showMessageBox("Sản phầm này không đủ số lượng để cung cấp");
                        return;
                    }
                } // Số lượng ban đầu nhỏ hơn
                else {
                    // Cộng đi số lượng sản phẩm cần trừ
                    int canCong = hoaDonChiTietSelected.getSoLuong() - soLuong;
                    System.out.println("CAN CONG: " + canCong);

                    // Cập nhật số lượng
                    hoaDonChiTietSelected.setSoLuong(soLuong);
                    String statusUpdateHoaDon = hoaDonChiTietService.update(hoaDonChiTietSelected);
                    System.out.println("STATUS SUA SO LUONG TRONG HOA DON: " + statusUpdateHoaDon);
                    loadHoaDonChiTietByMaHoaDon();

                    // Cộng lại vào spct
                    sanPhamChiTietSeleted.setSoLuong(sanPhamChiTietSeleted.getSoLuong() + canCong);
                    System.out.println(sanPhamChiTietSeleted);
                    String statusSuaSoLuong = sanPhamChiTietService.update(sanPhamChiTietSeleted);
                    System.out.println("STATUS SUA SO LUONG TRONG SPCT: " + statusSuaSoLuong);
                    loadSanPhamChiTietToTable();

                }
                reloadInfoHoaDonByMaHoaDon();
            }
        }

    }

    private void setPopupMenuGioHang() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem jMenuThemVaoGioHang = new JMenuItem("Sửa số lượng");
        JMenuItem jMenuXoaKhoiGioHang = new JMenuItem("Xóa khỏi giỏ hàng");
        jMenuThemVaoGioHang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                funcSuaSoLuong();
            }
        }
        );
        jMenuXoaKhoiGioHang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                funcXoaKhoiGio();
            }
        }
        );
        popupMenu.add(jMenuThemVaoGioHang);
        popupMenu.add(jMenuXoaKhoiGioHang);
        // Attach the popup menu to the JTable

        tblGioHang.setComponentPopupMenu(popupMenu);
    }

    private void funcXoaKhoiGio() {
        if (INDEX_ROW_SELECTED_HDCT < 0) {
            showMessageBox("Vui lòng chọn 1 sản phẩm trước");
            return;
        } else {
            HoaDonChiTiet hoaDonChiTiet = listHoaDonChiTiet.get(INDEX_ROW_SELECTED_HDCT);
            System.out.println("HDCT: " + hoaDonChiTiet);
            xoaHoaDonChiTietKhoiGioHang();
        }
    }

    private void funcSuaSoLuong() {
        if (INDEX_ROW_SELECTED_HDCT < 0) {
            showMessageBox("Vui lòng chọn 1 sản phẩm trước");
            return;
        } else {
            HoaDonChiTiet hoaDonChiTiet = listHoaDonChiTiet.get(INDEX_ROW_SELECTED_HDCT);
            System.out.println("HDCT: " + hoaDonChiTiet);
            System.out.println("Call Them Edit");
            updateAgainSoLuong();
        }

    }

    private void funcThemVaoGioHang() {
        if (GLOBAL_MA_HOA_DON == 0) {
            showMessageBox("Vui Lòng chọn hóa đơn chờ");
            return;
        } else {
            System.out.println("SELECTED ROW EVENT RIGHT CLICK: " + INDEX_ROW_SELECTED_SPCT);
            if (INDEX_ROW_SELECTED_SPCT < 0) {
                showMessageBox("Vui lòng chọn 1 sản phẩm trước");
                return;
            } else {
                System.out.println("Call Them Vao Gio Hang");
                callInputDialogSoLuong();
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
                funcThemVaoGioHang();
            }
        }
        );
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
                        loadHoaDonChiTietByMaHoaDon();

                        // Trừ đi số lượng sản phẩm chi tiết đang có
                        sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - soLuong);
                        System.out.println(sanPhamChiTiet);
                        String statusSuaSoLuong = sanPhamChiTietService.update(sanPhamChiTiet);
                        System.out.println("STATUS SUA SO LUONG TRONG SPCT: " + statusSuaSoLuong);
                        loadSanPhamChiTietToTable();

                    } else {
                        // Thêm mới
                        hoaDonChiTietService.add(hoaDonChiTiet);
                        loadHoaDonChiTietByMaHoaDon();

                        // Trừ đi số lượng sản phẩm chi tiết đang có
                        sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - soLuong);
                        System.out.println(sanPhamChiTiet);
                        String statusSuaSoLuong = sanPhamChiTietService.update(sanPhamChiTiet);
                        System.out.println("STATUS SUA SO LUONG TRONG SPCT: " + statusSuaSoLuong);
                        loadSanPhamChiTietToTable();

                    }
                    reloadInfoHoaDonByMaHoaDon();
                }
            }
        }

    }

    public void showMessageBox(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void loadHoaDonChiTietByMaHoaDon() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Mã sản phẩm", "Tên sản phẩm", "Hãng", "Màu sắc", "Size", "Chất liệu", "Đơn giá", "Số lượng", "Thành tiền"});
        listHoaDonChiTiet.clear();
        if (GLOBAL_MA_HOA_DON != 0) {
            listHoaDonChiTiet = hoaDonChiTietService.selectAllByMaHoaDon(GLOBAL_MA_HOA_DON);
            for (HoaDonChiTiet hoaDonChiTiet : listHoaDonChiTiet) {
                SanPham sanPham = sanPhamService.findById(sanPhamChiTietService.findById(hoaDonChiTiet.getMaSanPhamChiTiet()).getMaSanPham());
                SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietService.findById(hoaDonChiTiet.getMaSanPhamChiTiet());
                Hang hang = hangService.findById(sanPhamChiTiet.getMaHang());
                MauSac mauSac = mauSacService.findById(sanPhamChiTiet.getMaMauSac());
                Size size = sizeService.findById(sanPhamChiTiet.getMaSize());
                ChatLieu chatLieu = chatLieuService.findById(sanPhamChiTiet.getMaChatLieu());
                String tenSanPham = sanPham.getTenSanPham();
                String donGia = String.valueOf(sanPhamChiTietService.findById(hoaDonChiTiet.getMaSanPhamChiTiet()).getGiaSanPham());
                model.addRow(new Object[]{hoaDonChiTiet.getMaSanPhamChiTiet(), tenSanPham, hang.getTenHang(), mauSac.getTenMauSac(), size.getTenSize(), chatLieu.getTenChatLieu(), donGia, hoaDonChiTiet.getSoLuong(), String.valueOf(Float.valueOf(donGia) * hoaDonChiTiet.getSoLuong())});
            }
        }
        tblGioHang.setModel(model);
    }

    public String genSqlQueryAfterBoLoc(String baseSql) {
        SanPham locSanPham = (SanPham) cboSanPham.getSelectedItem();
        Hang locHang = ((Hang) cboHang.getSelectedItem());
        ChatLieu locChatLieu = ((ChatLieu) cboChatLieu.getSelectedItem());
        Size locSize = ((Size) cboSize.getSelectedItem());
        MauSac locMauSac = ((MauSac) cboSanPham.getSelectedItem());
        System.out.println("==============================");
        System.out.println("Bo Loc Hang     : " + locHang);
        System.out.println("Bo Loc Chat Lieu: " + locChatLieu);
        System.out.println("Bo Loc Size     : " + locSize);
        System.out.println("Bo Loc Mau Sac  : " + locMauSac);
        System.out.println("==============================");
        int index = 0;
        if (locHang != null) {
            if (!baseSql.contains("WHERE")) {
                baseSql += " WHERE (";
            }
            baseSql += String.format(" MAHANG = %s", locHang.getMaHang());
            index += 1;
        }
        if (locChatLieu != null) {
            if (!baseSql.contains("WHERE")) {
                baseSql += " WHERE (";
            }
            if (index > 0) {
                baseSql += " AND";
            }
            baseSql += String.format(" MACHATLIEU = %s", locChatLieu.getMaChatLieu());
            index += 1;
        }
        if (locSize != null) {
            if (!baseSql.contains("WHERE")) {
                baseSql += " WHERE (";
            }
            if (index > 0) {
                baseSql += " AND";
            }
            baseSql += String.format(" MASIZE = %s", locSize.getMaSize());
            index += 1;
        }
        if (locMauSac != null) {
            if (!baseSql.contains("WHERE")) {
                baseSql += " WHERE (";
            }
            if (index > 0) {
                baseSql += " AND";
            }
            baseSql += String.format(" MAMAUSAC = %s", locMauSac.getMaMauSac());
            index += 1;
        }
        if (locSanPham != null) {
            if (!baseSql.contains("WHERE")) {
                baseSql += " WHERE (";
            }
            if (index > 0) {
                baseSql += " AND";
            }
            baseSql += String.format(" MASANPHAM = %s", locMauSac.getMaMauSac());
            index += 1;
        }
        baseSql += " )";
        return baseSql;

    }

    public Object[] getListSPCTByBoLoc() {
        String baseSql = "SELECT * FROM SANPHAMCHITIET";
        baseSql = genSqlQueryAfterBoLoc(baseSql);
        int totalItem = sanPhamChiTietService.selectAllByCustomSql(baseSql).size();
        baseSql += String.format(" ORDER BY MASANPHAMCHITIET OFFSET %s ROWS FETCH NEXT %s ROWS ONLY ;", CONFIG_LIMIT_DATA_PAGE * (INDEX_SELECT_PAGE_SPCT - 1), CONFIG_LIMIT_DATA_PAGE);
        System.out.println(baseSql);
        return new Object[]{sanPhamChiTietService.selectAllByCustomSql(baseSql), totalItem};

    }

    public void loadSanPhamChiTietToTable() {
        String searchText = txtSearch.getText().toLowerCase();
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

        } else {
            baseSql = "SELECT * FROM [SanPhamChiTiet] JOIN SanPham ON SanPhamChiTiet.maSanPham = SanPham.maSanPham JOIN HANG ON SanPhamChiTiet.maHang = Hang.maHang JOIN MauSac ON SanPhamChiTiet.maMauSac = MauSac.maMauSac JOIN Size ON SanPhamChiTiet.maSize = Size.maSize JOIN ChatLieu ON SanPhamChiTiet.maChatLieu = ChatLieu.maChatLieu";
        }

        if (cboSanPham.getSelectedItem() != null) {
            SanPham sanPhamSelected = ((SanPham) cboSanPham.getSelectedItem());
            baseSql += String.format(" AND SanPhamChiTiet.MASANPHAM = %s", sanPhamSelected.getMaSanPham());
        }
        if (cboHang.getSelectedItem() != null) {
            Hang hangSelected = ((Hang) cboHang.getSelectedItem());
            baseSql += String.format(" AND SanPhamChiTiet.MAHANG = %s", hangSelected.getMaHang());
        }
        if (cboMauSac.getSelectedItem() != null) {
            MauSac mauSacSeleted = ((MauSac) cboMauSac.getSelectedItem());
            baseSql += String.format(" AND SanPhamChiTiet.MAMAUSAC = %s", mauSacSeleted.getMaMauSac());
        }
        if (cboChatLieu.getSelectedItem() != null) {
            ChatLieu chatLieuSelected = ((ChatLieu) cboChatLieu.getSelectedItem());
            baseSql += String.format(" AND SanPhamChiTiet.MACHATLIEU = %s", chatLieuSelected.getMaChatLieu());
        }
        if (cboSize.getSelectedItem() != null) {
            Size sizeSelected = ((Size) cboSize.getSelectedItem());
            baseSql += String.format(" AND SanPhamChiTiet.MASIZE = %s", sizeSelected.getMaSize());
        }
        //
        int totalItem = sanPhamChiTietService.selectAllByCustomSql(baseSql).size();
        pagination1.setPagegination(INDEX_SELECT_PAGE_SPCT, (int) Math.ceil((float) totalItem / CONFIG_LIMIT_DATA_PAGE));
        //
        baseSql += String.format(" ORDER BY SanPhamChiTiet.MASANPHAMCHITIET OFFSET %s ROWS FETCH NEXT %s ROWS ONLY ;", CONFIG_LIMIT_DATA_PAGE * (INDEX_SELECT_PAGE_SPCT - 1), CONFIG_LIMIT_DATA_PAGE);

        System.out.println("BASE SQL: " + baseSql);
        listSanPhamChiTiet = sanPhamChiTietService.selectAllByCustomSql(baseSql);
        addRowToTblSanPhamChiTiet(listSanPhamChiTiet);

    }

    public void addRowToTblSanPhamChiTiet(List<SanPhamChiTiet> listSanPhamChiTiet) {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Mã Sản Phẩm Chi Tiết", "Tên Sản Phẩm", "Hãng", "Màu Sắc", "Size", "Chất Liệu", "Số Lượng", "Giá"});
        for (SanPhamChiTiet sanPhamChiTiet : listSanPhamChiTiet) {
            String maSanPhamChiTiet = String.valueOf(sanPhamChiTiet.getMaSanPhamChiTiet());
            String tenSanPham = String.valueOf(sanPhamService.findById(sanPhamChiTiet.getMaSanPham()).toString());
            String tenHang = hangService.findById(sanPhamChiTiet.getMaHang()).getTenHang();
            String tenMauSac = mauSacService.findById(sanPhamChiTiet.getMaMauSac()).getTenMauSac();
            String tenSize = sizeService.findById(sanPhamChiTiet.getMaSize()).getTenSize();
            String tenChatLieu = chatLieuService.findById(sanPhamChiTiet.getMaChatLieu()).getTenChatLieu();
            model.addRow(new Object[]{maSanPhamChiTiet, tenSanPham, tenHang, tenMauSac, tenSize, tenChatLieu, sanPhamChiTiet.getSoLuong(), sanPhamChiTiet.getGiaSanPham()});
        }
        tblSanPhamChiTiet.setModel(model);

    }

    public void init_loadSanPhamToComboBox() {
        //Load San Pham
        List<SanPham> listSanPham = sanPhamService.selectAll();
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        cboSanPham.setPrototypeDisplayValue("XXXXXXXXXXXX");
        model.addElement(null);
        for (SanPham sanPham : listSanPham) {
            model.addElement(sanPham);
        }
        cboSanPham.setModel(model);

        //Load Thuộc Tính Hãng
        List<Hang> listHang = hangService.selectAll();
        DefaultComboBoxModel model_1 = new DefaultComboBoxModel();
        model_1.addElement(null);
        for (Hang hang : listHang) {
            model_1.addElement(hang);
        }
        cboHang.setModel(model_1);

        //Load Thuộc Tính Màu Sắc
        List<MauSac> listMauSac = mauSacService.selectAll();
        DefaultComboBoxModel model_2 = new DefaultComboBoxModel();
        model_2.addElement(null);
        for (MauSac mauSac : listMauSac) {
            model_2.addElement(mauSac);
        }
        cboMauSac.setModel(model_2);

        //Load Thuộc Tính Chất Liệu
        List<ChatLieu> listChatLieu = chatLieuService.selectAll();
        DefaultComboBoxModel model_3 = new DefaultComboBoxModel();
        model_3.addElement(null);
        for (ChatLieu chatLieu : listChatLieu) {
            model_3.addElement(chatLieu);
        }
        cboChatLieu.setModel(model_3);

        //Load Thuộc Tính Size
        List<Size> listSize = sizeService.selectAll();
        DefaultComboBoxModel model_4 = new DefaultComboBoxModel();
        model_4.addElement(null);
        for (Size size : listSize) {
            model_4.addElement(size);
        }
        cboSize.setModel(model_4);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
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
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblHoaDonCho = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblSanPhamChiTiet = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        pagination1 = new com.raven.component.Pagination();
        jPanel6 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        cboSanPham = new javax.swing.JComboBox<>();
        jPanel10 = new javax.swing.JPanel();
        cboHang = new javax.swing.JComboBox<>();
        jPanel9 = new javax.swing.JPanel();
        cboChatLieu = new javax.swing.JComboBox<>();
        jPanel11 = new javax.swing.JPanel();
        cboMauSac = new javax.swing.JComboBox<>();
        jPanel8 = new javax.swing.JPanel();
        cboSize = new javax.swing.JComboBox<>();
        jPanel12 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblGioHang = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setBackground(new java.awt.Color(242, 242, 242));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Hóa đơn chờ"));

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

        jButton1.setText("Áp mã");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setText("Thanh Toan");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbMaHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbTongTienTruoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbMaGiamGia, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbTenKhachHang, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbTongGiamGia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbTongThanhToan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton3)))
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tblSanPhamChiTietMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblSanPhamChiTietMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblSanPhamChiTietMouseReleased(evt);
            }
        });
        jScrollPane3.setViewportView(tblSanPhamChiTiet);

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));

        pagination1.setOpaque(false);
        jPanel1.add(pagination1);

        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.LINE_AXIS));

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Sản phẩm", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        cboSanPham.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboSanPham.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboSanPhamItemStateChanged(evt);
            }
        });
        cboSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSanPhamActionPerformed(evt);
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
            .addComponent(cboSanPham, javax.swing.GroupLayout.Alignment.TRAILING, 0, 209, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(cboSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel6.add(jPanel5);

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Hãng", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        cboHang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboHang.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboHangItemStateChanged(evt);
            }
        });
        cboHang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cboHangKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cboHang, 0, 181, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(cboHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel6.add(jPanel10);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Chất liệu"));

        cboChatLieu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboChatLieu.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboChatLieuItemStateChanged(evt);
            }
        });
        cboChatLieu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cboChatLieuKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cboChatLieu, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(cboChatLieu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel6.add(jPanel9);

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Màu sắc"));

        cboMauSac.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboMauSac.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboMauSacItemStateChanged(evt);
            }
        });
        cboMauSac.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cboMauSacKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cboMauSac, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(cboMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel6.add(jPanel11);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Size", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        cboSize.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboSize.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboSizeItemStateChanged(evt);
            }
        });
        cboSize.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cboSizeKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cboSize, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(cboSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel6.add(jPanel8);

        jPanel12.setLayout(new javax.swing.BoxLayout(jPanel12, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setText("Tìm kiếm: ");
        jPanel12.add(jLabel1);

        txtSearch.setBorder(null);
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });
        jPanel12.add(txtSearch);

        jButton7.setText("Thêm vào giỏ hàng");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jButton7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Giỏ hàng"));
        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.LINE_AXIS));

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
        tblGioHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblGioHangMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblGioHang);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
        );

        jPanel7.add(jPanel14);

        jButton6.setText("Sửa số lượng");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton2.setText("Xóa khỏi giỏ");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel13Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton2, jButton6});

        jPanel7.add(jPanel13);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
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

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        // TODO add your handling code here:
        loadSanPhamChiTietToTable();
    }//GEN-LAST:event_txtSearchKeyReleased

    private void tblHoaDonChoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonChoMousePressed
        // TODO add your handling code here:
        INDEX_ROW_SELECTED_HDC = tblHoaDonCho.getSelectedRow();

        GLOBAL_MA_HOA_DON = Integer.valueOf(tblHoaDonCho.getValueAt(INDEX_ROW_SELECTED_HDC, 0).toString());
        reloadInfoHoaDonByMaHoaDon();
        loadHoaDonChiTietByMaHoaDon();

    }//GEN-LAST:event_tblHoaDonChoMousePressed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        openDialogSelectKhachHang();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        calledHoaDon();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void cboSizeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboSizeItemStateChanged
        // TODO add your handling code here:
        loadSanPhamChiTietToTable();
    }//GEN-LAST:event_cboSizeItemStateChanged

    private void cboSizeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboSizeKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_cboSizeKeyReleased

    private void cboChatLieuItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboChatLieuItemStateChanged
        // TODO add your handling code here:
        loadSanPhamChiTietToTable();
    }//GEN-LAST:event_cboChatLieuItemStateChanged

    private void cboChatLieuKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboChatLieuKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_cboChatLieuKeyReleased

    private void cboHangItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboHangItemStateChanged
        // TODO add your handling code here:
        loadSanPhamChiTietToTable();
    }//GEN-LAST:event_cboHangItemStateChanged

    private void cboHangKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboHangKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_cboHangKeyReleased

    private void cboMauSacItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboMauSacItemStateChanged
        // TODO add your handling code here:
        loadSanPhamChiTietToTable();
    }//GEN-LAST:event_cboMauSacItemStateChanged

    private void cboMauSacKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboMauSacKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_cboMauSacKeyReleased

    private void tblSanPhamChiTietMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSanPhamChiTietMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_tblSanPhamChiTietMouseExited

    private void tblSanPhamChiTietMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSanPhamChiTietMouseReleased
        // TODO add your handling code here:
        System.out.println("@2222");
    }//GEN-LAST:event_tblSanPhamChiTietMouseReleased

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        thanhToanHoaDon();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        openDialogSelectKhuyenMai();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cboSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSanPhamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboSanPhamActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        funcSuaSoLuong();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        funcXoaKhoiGio();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        funcThemVaoGioHang();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void tblGioHangMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGioHangMousePressed
        // TODO add your handling code here:
        INDEX_ROW_SELECTED_HDCT = tblGioHang.getSelectedRow();
    }//GEN-LAST:event_tblGioHangMousePressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cboChatLieu;
    private javax.swing.JComboBox<String> cboHang;
    private javax.swing.JComboBox<String> cboMauSac;
    private javax.swing.JComboBox<String> cboSanPham;
    private javax.swing.JComboBox<String> cboSize;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lbMaGiamGia;
    private javax.swing.JLabel lbMaHoaDon;
    private javax.swing.JLabel lbTenKhachHang;
    private javax.swing.JLabel lbTongGiamGia;
    private javax.swing.JLabel lbTongThanhToan;
    private javax.swing.JLabel lbTongTienTruoc;
    private com.raven.component.Pagination pagination1;
    private javax.swing.JTable tblGioHang;
    private javax.swing.JTable tblHoaDonCho;
    private javax.swing.JTable tblSanPhamChiTiet;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
