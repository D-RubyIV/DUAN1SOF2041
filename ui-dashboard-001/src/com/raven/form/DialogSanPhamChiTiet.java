/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.Array;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author phamh
 */
public class DialogSanPhamChiTiet extends javax.swing.JDialog {

    /**
     * Creates new form DialogSanPhamChiTiet
     */
    private HangService hangService = new HangService();
    private MauSacService mauSacService = new MauSacService();
    private ChatLieuService chatLieuService = new ChatLieuService();
    private SizeService sizeService = new SizeService();
    private SanPhamService sanPhamService = new SanPhamService();
    private SanPhamChiTietService sanPhamChiTietService = new SanPhamChiTietService();
    List<SanPhamChiTiet> listSanPhamChiTiet = new ArrayList<>();
    List<SanPhamChiTiet> listSanPhamChiTietBoLoc = new ArrayList<>();
    int INDEX_SELECT_ROW_BANG_SAN_PHAM_CHI_TIET = -1;
    int INDEX_SELECT_PAGE_SPCT = 1;
    int CONFIG_LIMIT_DATA_PAGE = 5;
    String NAME_FILE_IMAGE_SELECTED = "";
    int GLOBAL_MA_SANPHAM = 0;

    public DialogSanPhamChiTiet(java.awt.Frame parent, boolean modal, int maSanPham) {
        super(parent, modal);
        initComponents();
        GLOBAL_MA_SANPHAM = maSanPham;
        setLocationRelativeTo(null);
        loadComboBoxBoLoc();
        loadThuocTinhToComboBoxTrongTabSanPhamChiTiet();
        init_LoadSanPhamChiTietToTabble();

        pagination1.setPaginationItemRender(new PaginationItemRenderStyle1());
        pagination1.addEventPagination(new EventPagination() {
            @Override
            public void pageChanged(int page) {
                INDEX_SELECT_PAGE_SPCT = page;
                init_LoadSanPhamChiTietToTabble();
            }
        });
    }

    public String genSqlQueryAfterBoLoc(String baseSql) {
        Hang locHang = ((Hang) cboLocHang.getSelectedItem());
        ChatLieu locChatLieu = ((ChatLieu) cboLocChatLieu.getSelectedItem());
        Size locSize = ((Size) cboLocSize.getSelectedItem());
        MauSac locMauSac = ((MauSac) cboLocMau.getSelectedItem());
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
        if (index > 0) {
            baseSql += String.format(" AND MASANPHAM = %s )", GLOBAL_MA_SANPHAM);
        }
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

    public void init_LoadSanPhamChiTietToTabble() {
        String searchText = txtTimKiem.getText();
        Hang locHang = ((Hang) cboLocHang.getSelectedItem());
        ChatLieu locChatLieu = ((ChatLieu) cboLocChatLieu.getSelectedItem());
        Size locSize = ((Size) cboLocSize.getSelectedItem());
        MauSac locMauSac = ((MauSac) cboLocMau.getSelectedItem());
        // By Bo Loc
        if (locHang != null || locChatLieu != null || locSize != null || locMauSac != null) {
            Object[] result = getListSPCTByBoLoc();
            listSanPhamChiTiet = (List<SanPhamChiTiet>) result[0];
            int totalItem = (int) result[1];
            System.out.println("Size: " + listSanPhamChiTiet.size());
            pagination1.setPagegination(INDEX_SELECT_PAGE_SPCT, (int) Math.ceil(totalItem / CONFIG_LIMIT_DATA_PAGE));
        }
        if (!searchText.isEmpty()) {
            // By Search
            String textSearch = txtTimKiem.getText().toLowerCase();
            System.out.println("Search Text: " + textSearch);
            String baseSql = "SELECT * FROM [SanPhamChiTiet] "
                    + "JOIN SanPham ON SanPhamChiTiet.maSanPham = SanPham.maSanPham "
                    + "JOIN HANG ON SanPhamChiTiet.maHang = Hang.maHang "
                    + "JOIN MauSac ON SanPhamChiTiet.maMauSac = MauSac.maMauSac "
                    + "JOIN Size ON SanPhamChiTiet.maSize = Size.maSize "
                    + "JOIN ChatLieu ON SanPhamChiTiet.maChatLieu = ChatLieu.maChatLieu "
                    + "WHERE ( tenChatLieu LIKE N'%" + textSearch + "%' "
                    + "OR tenSize LIKE N'%" + textSearch + "%' "
                    + "OR tenHang LIKE N'%" + textSearch + "%' "
                    + "OR tenMauSac LIKE N'%" + textSearch + "%') "
                    + "AND SanPhamChiTiet.MASANPHAM = " + GLOBAL_MA_SANPHAM;
            Hang locHangSearch = ((Hang) cboLocHang.getSelectedItem());
            ChatLieu locChatLieuSearch = ((ChatLieu) cboLocChatLieu.getSelectedItem());
            Size locSizeSearch = ((Size) cboLocSize.getSelectedItem());
            MauSac locMauSacSearch = ((MauSac) cboLocMau.getSelectedItem());
            System.out.println("==============================");
            System.out.println("Bo Loc Hang     : " + locHangSearch);
            System.out.println("Bo Loc Chat Lieu: " + locChatLieuSearch);
            System.out.println("Bo Loc Size     : " + locSizeSearch);
            System.out.println("Bo Loc Mau Sac  : " + locMauSacSearch);
            System.out.println("==============================");
            if (locHangSearch != null) {
                baseSql += String.format(" AND SanPhamChiTiet.MAHANG = %s", locHang.getMaHang());
            }
            if (locChatLieuSearch != null) {
                baseSql += String.format(" AND SanPhamChiTiet.MACHATLIEU = %s", locChatLieu.getMaChatLieu());
            }
            if (locSizeSearch != null) {
                baseSql += String.format(" AND SanPhamChiTiet.MASIZE = %s", locSize.getMaSize());
            }
            if (locMauSacSearch != null) {
                baseSql += String.format(" AND SanPhamChiTiet.MAMAUSAC = %s", locMauSac.getMaMauSac());
            }

            System.out.println(baseSql);
            int totalItem = sanPhamChiTietService.selectAllByCustomSql(baseSql).size();
            baseSql += String.format(" ORDER BY SanPhamChiTiet.MASANPHAMCHITIET OFFSET %s ROWS FETCH NEXT %s ROWS ONLY ;", CONFIG_LIMIT_DATA_PAGE * (INDEX_SELECT_PAGE_SPCT - 1), CONFIG_LIMIT_DATA_PAGE);

            pagination1.setPagegination(INDEX_SELECT_PAGE_SPCT, (int) Math.ceil(totalItem / CONFIG_LIMIT_DATA_PAGE));
            listSanPhamChiTiet = sanPhamChiTietService.selectAllByCustomSql(baseSql);

        }
        if (locHang == null && locChatLieu == null && locSize == null && locMauSac == null && searchText.isEmpty()) {
            // Nguyen Mau
            pagination1.setPagegination(INDEX_SELECT_PAGE_SPCT, (int) Math.ceil(sanPhamChiTietService.selectAllByMaSanPham(GLOBAL_MA_SANPHAM).size() / CONFIG_LIMIT_DATA_PAGE) + 1);
            listSanPhamChiTiet = sanPhamChiTietService.selectAllFromAToB(CONFIG_LIMIT_DATA_PAGE * (INDEX_SELECT_PAGE_SPCT - 1), CONFIG_LIMIT_DATA_PAGE, GLOBAL_MA_SANPHAM);

        }
        addRowToTableByListSPCT(listSanPhamChiTiet);
    }

    public void loadComboBoxBoLoc() {
        List<Hang> listBoLocHang = hangService.selectAll();
        DefaultComboBoxModel modelCboLocHang = new DefaultComboBoxModel();
        modelCboLocHang.addElement(null);
        for (Hang hang : listBoLocHang) {
            modelCboLocHang.addElement(hang);
        }
        cboLocHang.setModel(modelCboLocHang);

        List<ChatLieu> listBoLocChatLieu = chatLieuService.selectAll();
        DefaultComboBoxModel modelCboLocChatLieu = new DefaultComboBoxModel();
        modelCboLocChatLieu.addElement(null);
        for (ChatLieu chatLieu : listBoLocChatLieu) {
            modelCboLocChatLieu.addElement(chatLieu);
        }
        cboLocChatLieu.setModel(modelCboLocChatLieu);

        List<MauSac> listBoLocMauSac = mauSacService.selectAll();
        DefaultComboBoxModel modelCboLocMauSac = new DefaultComboBoxModel();
        modelCboLocMauSac.addElement(null);
        for (MauSac mauSac : listBoLocMauSac) {
            modelCboLocMauSac.addElement(mauSac);
        }
        cboLocMau.setModel(modelCboLocMauSac);

        List<Size> listBoLocSize = sizeService.selectAll();
        DefaultComboBoxModel modelCboLocSize = new DefaultComboBoxModel();
        modelCboLocSize.addElement(null);
        for (Size size : listBoLocSize) {
            modelCboLocSize.addElement(size);
        }
        cboLocSize.setModel(modelCboLocSize);

    }

    public void OpenDialogThuocTinh(String loaiThuocTinh) {
        DialogThuocTinhSanPham dialogThuocTinhSanPham = new DialogThuocTinhSanPham(new javax.swing.JFrame(), true, loaiThuocTinh);
        dialogThuocTinhSanPham.setVisible(true);
        dialogThuocTinhSanPham.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("Dialog đã đóng");
                loadComboBoxBoLoc();
                loadThuocTinhToComboBoxTrongTabSanPhamChiTiet();
            }
        });
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

        //Load Thuộc Tính Size
        List<Size> listSize = sizeService.selectAll();
        DefaultComboBoxModel model_4 = new DefaultComboBoxModel();
        for (Size size : listSize) {
            model_4.addElement(size);
        }
        cboSize.setModel(model_4);

        //Load Thuộc Tính Hãng
        SanPham sanPham = sanPhamService.findById(GLOBAL_MA_SANPHAM);
        DefaultComboBoxModel model_5 = new DefaultComboBoxModel();
        model_5.addElement(sanPham);
        cboSanPham.setModel(model_5);
        cboSanPham.setEnabled(false);

    }

    public void addRowToTableByListSPCT(List<SanPhamChiTiet> listSanPhamChiTiet) {
//        tblBangSanPhamChiTiet.getValueAt(ERROR, WIDTH)
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Stt", "Mã Sản Phẩm Chi Tiết", "Tên Sản Phẩm", "Tên Hãng", "Tên Màu Sắc", "Tên Size", "Tên Chất Liệu", "Số Luọng", "Mô Tả", "Giá", "Hình ẢNh"});
        int index = 0;
        for (SanPhamChiTiet sanPhamChiTiet : listSanPhamChiTiet) {
            index += 1;
            String tenSanPham = sanPhamService.findById(sanPhamChiTiet.getMaSanPham()).getTenSanPham();
            String tenHang = hangService.findById(sanPhamChiTiet.getMaHang()).getTenHang();
            String tenMauSac = mauSacService.findById(sanPhamChiTiet.getMaMauSac()).getTenMauSac();
            String tenSize = sizeService.findById(sanPhamChiTiet.getMaSize()).getTenSize();
            String tenChatLieu = chatLieuService.findById(sanPhamChiTiet.getMaChatLieu()).getTenChatLieu();
            model.addRow(new Object[]{index, sanPhamChiTiet.getMaSanPhamChiTiet(), tenSanPham, tenHang, tenMauSac, tenSize, tenChatLieu, sanPhamChiTiet.getSoLuong(), sanPhamChiTiet.getMota(), sanPhamChiTiet.getGiaSanPham(), sanPhamChiTiet.getHinhAnh()});
        }
        tblBangSanPhamChiTiet.setModel(model);
    }

    public void updateSanPhamChiTiet() {
        SanPhamChiTiet sanPhamChiTiet = getObjSanPhamChiTiet();
        if (sanPhamChiTiet != null) {
            showMessageBox(sanPhamChiTietService.update(sanPhamChiTiet));
            init_LoadSanPhamChiTietToTabble();
        }
    }

    public SanPhamChiTiet getObjSanPhamChiTiet() {
        int maSanPhamChiTiet = 0;
        try {
            maSanPhamChiTiet = Integer.parseInt(tblBangSanPhamChiTiet.getValueAt(INDEX_SELECT_ROW_BANG_SAN_PHAM_CHI_TIET, 1).toString());
        } catch (Exception e) {
            maSanPhamChiTiet = 0;
        }
        
        int maSanPham = ((SanPham) cboSanPham.getSelectedItem()).getMaSanPham();
        int maHang = ((Hang) cboHang.getSelectedItem()).getMaHang();
        int maMauSac = ((MauSac) cboMauSac.getSelectedItem()).getMaMauSac();
        int maSize = ((Size) cboSize.getSelectedItem()).getMaSize();
        int maChatLieu = ((ChatLieu) cboChatLieu.getSelectedItem()).getMaChatLieu();
        String moTa = txtMoTa.getText();
        String soLuong = txtSoLuong.getText();
        String giaSanPhamString = txtGia.getText();

        if (soLuong.isEmpty()) {
            showMessageBox("Vui lòng nhập số lượng");
            return null;
        }
        if (!soLuong.matches("\\d+")) {
            showMessageBox("Số lượng phải là số nguyên");
            return null;
        }
        if (Integer.valueOf(soLuong) < 0) {
            showMessageBox("Số lượng phải là số lớn hơn không");
            return null;
        }
        if (giaSanPhamString.isEmpty()) {
            showMessageBox("Vui lòng điền giá");
            return null;
        }
        try {
            float giaSanPham = Float.parseFloat(giaSanPhamString);
            return new SanPhamChiTiet(maSanPhamChiTiet, maSanPham, maHang, maMauSac, maSize, maChatLieu, Integer.valueOf(soLuong), giaSanPham, moTa, NAME_FILE_IMAGE_SELECTED);
//            return new SanPhamChiTiet(maChatLieu, maMauSac, maHang, maMauSac, maSize, maChatLieu, Integer.valueOf(soLuong), giaSanPham, moTa, NAME_FILE_IMAGE_SELECTED);
        } catch (Exception e) {
            showMessageBox("Giá sản phẩm phải là số");
            return null;
        }

    }

    public void addSanPhamChiTiet() {
        SanPhamChiTiet sanPhamChiTiet = getObjSanPhamChiTiet();
        if (sanPhamChiTiet != null) {
            showMessageBox(sanPhamChiTietService.add(sanPhamChiTiet));
            init_LoadSanPhamChiTietToTabble();
        }
    }

    public void deleteSanPhamChiTiet() {
        if (INDEX_SELECT_ROW_BANG_SAN_PHAM_CHI_TIET >= 0) {
            String maSanPhamChiTiet = tblBangSanPhamChiTiet.getValueAt(INDEX_SELECT_ROW_BANG_SAN_PHAM_CHI_TIET, 1).toString();
            showMessageBox(sanPhamChiTietService.delete(Integer.valueOf(maSanPhamChiTiet)));
            init_LoadSanPhamChiTietToTabble();
        } else {
            showMessageBox("Vui lòng chọn 1 trong các sản phẩm dưới bảng");
        }
    }

    public void searchEventSanPhamChiTiet() {
        String textSearch = txtTimKiem.getText();
        if (textSearch.isEmpty()) {
            INDEX_SELECT_PAGE_SPCT = 1;
        }
        init_LoadSanPhamChiTietToTabble();

    }

    public static String removeAccent(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        System.out.println(pattern.matcher(normalized).replaceAll("").toLowerCase());
        return pattern.matcher(normalized).replaceAll("").toLowerCase();
    }

    public void showMessageBox(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void choseImage() {
        JFileChooser fChose = new JFileChooser();
        int result = fChose.showOpenDialog(null);
        if (result == 0) {
            File f = fChose.getSelectedFile();
            XImage.save(f);
            SetImage(f.getName());
        }
    }

    public void SetImage(String nameFileImage) {
        NAME_FILE_IMAGE_SELECTED = nameFileImage;
        try {
            lbImage.setText("");
            File path = new File("images", nameFileImage);
            Image img = ImageIO.read(path);
            int labelWidth = lbImage.getWidth() - 20;
            int labelHeight = lbImage.getHeight() - 20;
            Image scaledImg = img.getScaledInstance(labelWidth, labelHeight, Image.SCALE_DEFAULT);
            ImageIcon imgIcon = new ImageIcon(scaledImg);
            lbImage.setIcon(imgIcon);
            lbImage.setToolTipText(path.getName());
        } catch (Exception e) {
            lbImage.setText("NULL");
            lbImage.setIcon(null);
        }
    }

    public void fillDataToFormSanPhamChiTiet() {
        INDEX_SELECT_ROW_BANG_SAN_PHAM_CHI_TIET = tblBangSanPhamChiTiet.getSelectedRow();
        SanPhamChiTiet sanPhamChiTiet = listSanPhamChiTiet.get(INDEX_SELECT_ROW_BANG_SAN_PHAM_CHI_TIET);
        DefaultComboBoxModel<Hang> modelHang = (DefaultComboBoxModel) cboHang.getModel();
        DefaultComboBoxModel<MauSac> modelMauSac = (DefaultComboBoxModel) cboMauSac.getModel();
        DefaultComboBoxModel<ChatLieu> modelChatLieu = (DefaultComboBoxModel) cboChatLieu.getModel();
        DefaultComboBoxModel<Size> modelSize = (DefaultComboBoxModel) cboSize.getModel();
        txtGia.setText(String.valueOf(sanPhamChiTiet.getGiaSanPham()));
        txtSoLuong.setText(String.valueOf(sanPhamChiTiet.getSoLuong()));
        txtMoTa.setText(sanPhamChiTiet.getMota());
        SetImage(sanPhamChiTiet.getHinhAnh());
        for (int i = 0; i < modelHang.getSize(); i++) {
            if (modelHang.getElementAt(i).getMaHang() == sanPhamChiTiet.getMaHang()) {
                cboHang.setSelectedIndex(i);
                break;
            }
        }
        for (int i = 0; i < modelMauSac.getSize(); i++) {
            if (modelMauSac.getElementAt(i).getMaMauSac() == sanPhamChiTiet.getMaMauSac()) {
                cboMauSac.setSelectedIndex(i);
                break;
            }
        }
        for (int i = 0; i < modelChatLieu.getSize(); i++) {
            if (modelChatLieu.getElementAt(i).getMaChatLieu() == sanPhamChiTiet.getMaChatLieu()) {
                cboChatLieu.setSelectedIndex(i);
                break;
            }
        }
        for (int i = 0; i < modelSize.getSize(); i++) {
            if (modelSize.getElementAt(i).getMaSize() == sanPhamChiTiet.getMaSize()) {
                cboSize.setSelectedIndex(i);
                break;
            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        txtTimKiem = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        pagination1 = new com.raven.component.Pagination();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblBangSanPhamChiTiet = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        cboLocChatLieu = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        cboLocHang = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        cboLocMau = new javax.swing.JComboBox<>();
        jPanel8 = new javax.swing.JPanel();
        cboLocSize = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtGia = new javax.swing.JTextField();
        txtSoLuong = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtMoTa = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        cboMauSac = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        cboHang = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        cboChatLieu = new javax.swing.JComboBox<>();
        cboSanPham = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        cboSize = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        lbImage = new javax.swing.JLabel();
        btnAddTab1 = new javax.swing.JButton();
        btnUpdateTab1 = new javax.swing.JButton();
        btnClearTab1 = new javax.swing.JButton();
        btnDeleteTab1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Tìm Kiếm"));

        txtTimKiem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                txtTimKiemMouseReleased(evt);
            }
        });
        txtTimKiem.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtTimKiemInputMethodTextChanged(evt);
            }
        });
        txtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKiemActionPerformed(evt);
            }
        });
        txtTimKiem.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                txtTimKiemPropertyChange(evt);
            }
        });
        txtTimKiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTimKiemKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKiemKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(txtTimKiem)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11))
        );

        jPanel3.setBackground(new java.awt.Color(102, 102, 102));

        pagination1.setBackground(new java.awt.Color(102, 102, 255));
        pagination1.setForeground(new java.awt.Color(153, 153, 255));
        pagination1.setOpaque(false);
        jPanel3.add(pagination1);

        tblBangSanPhamChiTiet.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9", "Title 10"
            }
        ));
        tblBangSanPhamChiTiet.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblBangSanPhamChiTietMousePressed(evt);
            }
        });
        tblBangSanPhamChiTiet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblBangSanPhamChiTietKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(tblBangSanPhamChiTiet);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Bộ lọc"));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Chất liệu"));

        cboLocChatLieu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboLocChatLieu.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboLocChatLieuItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cboLocChatLieu, 0, 101, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(cboLocChatLieu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Hãng"));

        cboLocHang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboLocHang.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboLocHangItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cboLocHang, 0, 102, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cboLocHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Màu sắc"));

        cboLocMau.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboLocMau.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboLocMauItemStateChanged(evt);
            }
        });
        cboLocMau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLocMauActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cboLocMau, 0, 101, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cboLocMau)
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Size"));

        cboLocSize.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboLocSize.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboLocSizeItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cboLocSize, 0, 101, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(cboLocSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin sản phẩm"));

        jLabel9.setText("Mô Tả");

        jLabel7.setText("Giá");

        jLabel10.setText("Số Lượng");

        txtGia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGiaActionPerformed(evt);
            }
        });

        txtSoLuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSoLuongActionPerformed(evt);
            }
        });

        txtMoTa.setColumns(20);
        txtMoTa.setRows(5);
        jScrollPane1.setViewportView(txtMoTa);

        jLabel8.setText("Size:");

        cboMauSac.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel4.setText("Hãng:");

        cboHang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton1.setText("+");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("+");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel3.setText("Màu Sắc");

        jButton3.setText("+");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("+");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        cboChatLieu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cboSanPham.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel5.setText("Chất liệu:");

        cboSize.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel2.setText("Sản Phẩm");

        lbImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbImage.setText("Null");
        lbImage.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        lbImage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbImageMousePressed(evt);
            }
        });

        btnAddTab1.setText("Thêm");
        btnAddTab1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddTab1ActionPerformed(evt);
            }
        });

        btnUpdateTab1.setText("Sửa");
        btnUpdateTab1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateTab1ActionPerformed(evt);
            }
        });

        btnClearTab1.setText("Làm mới");
        btnClearTab1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearTab1ActionPerformed(evt);
            }
        });

        btnDeleteTab1.setText("Xóa");
        btnDeleteTab1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteTab1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(lbImage, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(cboSize, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(cboChatLieu, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(cboSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(cboMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(cboHang, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAddTab1)
                    .addComponent(btnUpdateTab1)
                    .addComponent(btnDeleteTab1)
                    .addComponent(btnClearTab1))
                .addGap(8, 8, 8))
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAddTab1, btnClearTab1, btnDeleteTab1, btnUpdateTab1});

        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(cboSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(cboMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(cboHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(cboChatLieu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(cboSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)
                            .addComponent(btnAddTab1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnUpdateTab1))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(btnDeleteTab1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnClearTab1))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)))
                .addGap(0, 8, Short.MAX_VALUE))
            .addComponent(lbImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(30, 30, 30))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtGiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGiaActionPerformed

    private void txtSoLuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSoLuongActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSoLuongActionPerformed

    private void btnUpdateTab1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateTab1ActionPerformed
        // TODO add your handling code here:
        updateSanPhamChiTiet();
    }//GEN-LAST:event_btnUpdateTab1ActionPerformed

    private void txtTimKiemMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTimKiemMouseReleased

    }//GEN-LAST:event_txtTimKiemMouseReleased

    private void txtTimKiemInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtTimKiemInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKiemInputMethodTextChanged

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKiemActionPerformed

    private void txtTimKiemPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_txtTimKiemPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKiemPropertyChange

    private void txtTimKiemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKiemKeyPressed

    private void txtTimKiemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemKeyReleased
        // TODO add your handling code here:
        searchEventSanPhamChiTiet();

    }//GEN-LAST:event_txtTimKiemKeyReleased

    private void btnAddTab1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddTab1ActionPerformed
        // TODO add your handling code here:
        addSanPhamChiTiet();
    }//GEN-LAST:event_btnAddTab1ActionPerformed

    private void lbImageMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbImageMousePressed
        // TODO add your handling code here:
        choseImage();
    }//GEN-LAST:event_lbImageMousePressed

    private void tblBangSanPhamChiTietMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBangSanPhamChiTietMousePressed
        // TODO add your handling code here:
        fillDataToFormSanPhamChiTiet();
    }//GEN-LAST:event_tblBangSanPhamChiTietMousePressed

    private void tblBangSanPhamChiTietKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblBangSanPhamChiTietKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblBangSanPhamChiTietKeyPressed

    private void btnDeleteTab1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteTab1ActionPerformed
        // TODO add your handling code here:
        deleteSanPhamChiTiet();
    }//GEN-LAST:event_btnDeleteTab1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        OpenDialogThuocTinh("Màu Sắc");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        OpenDialogThuocTinh("Hãng");
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        OpenDialogThuocTinh("Chất Liệu");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        OpenDialogThuocTinh("Size");
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btnClearTab1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearTab1ActionPerformed
        // TODO add your handling code here:
        lbImage.setIcon(null);
        NAME_FILE_IMAGE_SELECTED = "";
        txtGia.setText("");
        txtMoTa.setText("");
        txtSoLuong.setText("");
        txtTimKiem.setText("");
        init_LoadSanPhamChiTietToTabble();

    }//GEN-LAST:event_btnClearTab1ActionPerformed

    private void cboLocMauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLocMauActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboLocMauActionPerformed

    private void cboLocHangItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboLocHangItemStateChanged
        // TODO add your handling code here:
        init_LoadSanPhamChiTietToTabble();
    }//GEN-LAST:event_cboLocHangItemStateChanged

    private void cboLocChatLieuItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboLocChatLieuItemStateChanged
        // TODO add your handling code here:
        init_LoadSanPhamChiTietToTabble();
    }//GEN-LAST:event_cboLocChatLieuItemStateChanged

    private void cboLocMauItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboLocMauItemStateChanged
        // TODO add your handling code here:
        init_LoadSanPhamChiTietToTabble();
    }//GEN-LAST:event_cboLocMauItemStateChanged

    private void cboLocSizeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboLocSizeItemStateChanged
        // TODO add your handling code here:
        init_LoadSanPhamChiTietToTabble();
    }//GEN-LAST:event_cboLocSizeItemStateChanged

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddTab1;
    private javax.swing.JButton btnClearTab1;
    private javax.swing.JButton btnDeleteTab1;
    private javax.swing.JButton btnUpdateTab1;
    private javax.swing.JComboBox<String> cboChatLieu;
    private javax.swing.JComboBox<String> cboHang;
    private javax.swing.JComboBox<String> cboLocChatLieu;
    private javax.swing.JComboBox<String> cboLocHang;
    private javax.swing.JComboBox<String> cboLocMau;
    private javax.swing.JComboBox<String> cboLocSize;
    private javax.swing.JComboBox<String> cboMauSac;
    private javax.swing.JComboBox<String> cboSanPham;
    private javax.swing.JComboBox<String> cboSize;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbImage;
    private com.raven.component.Pagination pagination1;
    private javax.swing.JTable tblBangSanPhamChiTiet;
    private javax.swing.JTextField txtGia;
    private javax.swing.JTextArea txtMoTa;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables

}
