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
import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
    int INDEX_SELECT_ROW_BANG_SAN_PHAM_CHI_TIET = -1;
    int INDEX_SELECT_PAGE_SPCT = 0;
    int CONFIG_LIMIT_DATA_PAGE = 3;
    String NAME_FILE_IMAGE_SELECTED = "";
    int GLOBAL_MA_SANPHAM = 0;

    public DialogSanPhamChiTiet(java.awt.Frame parent, boolean modal, int maSanPham) {
        super(parent, modal);
        initComponents();
        GLOBAL_MA_SANPHAM = maSanPham;
        setLocationRelativeTo(null);

        init_LoadSanPhamChiTietToTabble();
        loadThuocTinhToComboBoxTrongTabSanPhamChiTiet();
        pagination1.setPaginationItemRender(new PaginationItemRenderStyle1());
        pagination1.setPagegination(1, 10);
        pagination1.addEventPagination(new EventPagination() {
            @Override
            public void pageChanged(int page) {
                INDEX_SELECT_PAGE_SPCT = page - 1;
                init_LoadSanPhamChiTietToTabble();
            }
        });
    }

    public void OpenDialogThuocTinh(String loaiThuocTinh) {
        DialogThuocTinhSanPham dialogThuocTinhSanPham = new DialogThuocTinhSanPham(new javax.swing.JFrame(), true, loaiThuocTinh);
        dialogThuocTinhSanPham.setVisible(true);
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
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Mã Sản Phẩm Chi Tiết", "Tên Sản Phẩm", "Tên Hãng", "Tên Màu Sắc", "Tên Size", "Tên Chất Liệu", "Số Luọng", "Mô Tả", "Giá", "Hình ẢNh"});
        for (SanPhamChiTiet sanPhamChiTiet : listSanPhamChiTiet) {
            String tenSanPham = sanPhamService.findById(sanPhamChiTiet.getMaSanPham()).getTenSanPham();
            String tenHang = hangService.findById(sanPhamChiTiet.getMaHang()).getTenHang();
            String tenMauSac = mauSacService.findById(sanPhamChiTiet.getMaMauSac()).getTenMauSac();
            String tenSize = sizeService.findById(sanPhamChiTiet.getMaSize()).getTenSize();
            String tenChatLieu = chatLieuService.findById(sanPhamChiTiet.getMaChatLieu()).getTenChatLieu();
            model.addRow(new Object[]{sanPhamChiTiet.getMaSanPhamChiTiet(), tenSanPham, tenHang, tenMauSac, tenSize, tenChatLieu, sanPhamChiTiet.getSoLuong(), sanPhamChiTiet.getMota(), sanPhamChiTiet.getGiaSanPham(), sanPhamChiTiet.getHinhAnh()});
        }
        tblBangSanPhamChiTiet.setModel(model);
    }

    public void init_LoadSanPhamChiTietToTabble() {
//        System.out.println("Change Page: " + INDEX_SELECT_PAGE_SPCT + "From: " + CONFIG_LIMIT_DATA_PAGE * INDEX_SELECT_PAGE_SPCT + "To: " + CONFIG_LIMIT_DATA_PAGE * INDEX_SELECT_PAGE_SPCT + CONFIG_LIMIT_DATA_PAGE);
        listSanPhamChiTiet = sanPhamChiTietService.selectAllFromAToB(CONFIG_LIMIT_DATA_PAGE * INDEX_SELECT_PAGE_SPCT, CONFIG_LIMIT_DATA_PAGE, GLOBAL_MA_SANPHAM);
        addRowToTableByListSPCT(listSanPhamChiTiet);
    }

    public void updateSanPhamChiTiet() {
        SanPhamChiTiet sanPhamChiTiet = getObjSanPhamChiTiet();
        if (sanPhamChiTiet != null) {
            showMessageBox(sanPhamChiTietService.update(sanPhamChiTiet));
            init_LoadSanPhamChiTietToTabble();
        }
    }

    public SanPhamChiTiet getObjSanPhamChiTiet() {
        int maSanPhamChiTiet = Integer.parseInt(tblBangSanPhamChiTiet.getValueAt(INDEX_SELECT_ROW_BANG_SAN_PHAM_CHI_TIET, 0).toString());
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
            String maSanPhamChiTiet = tblBangSanPhamChiTiet.getValueAt(INDEX_SELECT_ROW_BANG_SAN_PHAM_CHI_TIET, 0).toString();
            showMessageBox(sanPhamChiTietService.delete(Integer.valueOf(maSanPhamChiTiet)));
            init_LoadSanPhamChiTietToTabble();
        } else {
            showMessageBox("Vui lòng chọn 1 trong các sản phẩm dưới bảng");
        }
    }

    public void searchEventSanPhamChiTiet() {
        String textSearch = txtTimKiem.getText();
        if (textSearch.isEmpty()) {
            init_LoadSanPhamChiTietToTabble();
        }
    }

    public static String removeAccent(String input) {
        String normalized = java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD);
        normalized = normalized.replaceAll("[^\\p{ASCII}]", "");
        return normalized.toLowerCase();
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

        cboSanPham = new javax.swing.JComboBox<>();
        txtGia = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtSoLuong = new javax.swing.JTextField();
        cboSize = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtMoTa = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnClearTab1 = new javax.swing.JButton();
        cboHang = new javax.swing.JComboBox<>();
        btnUpdateTab1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        txtTimKiem = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        cboChatLieu = new javax.swing.JComboBox<>();
        btnAddTab1 = new javax.swing.JButton();
        lbImage = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        pagination1 = new com.raven.component.Pagination();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblBangSanPhamChiTiet = new javax.swing.JTable();
        cboMauSac = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        btnDeleteTab1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        cboFilter = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        cboSanPham.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txtGia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGiaActionPerformed(evt);
            }
        });

        jLabel5.setText("Chất liệu:");

        txtSoLuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSoLuongActionPerformed(evt);
            }
        });

        cboSize.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txtMoTa.setColumns(20);
        txtMoTa.setRows(5);
        jScrollPane1.setViewportView(txtMoTa);

        jLabel2.setText("Sản Phẩm");

        jLabel7.setText("Giá");

        btnClearTab1.setText("Làm mới");

        cboHang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnUpdateTab1.setText("Sửa");
        btnUpdateTab1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateTab1ActionPerformed(evt);
            }
        });

        jLabel3.setText("Màu Sắc");

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

        btnSearch.setText("Tìm Kiếm");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 578, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnSearch))
        );

        cboChatLieu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnAddTab1.setText("Thêm");
        btnAddTab1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddTab1ActionPerformed(evt);
            }
        });

        lbImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbImage.setText("Null");
        lbImage.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        lbImage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbImageMousePressed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(102, 102, 102));
        jPanel3.setLayout(new java.awt.GridLayout(1, 0));

        pagination1.setBackground(new java.awt.Color(102, 102, 255));
        pagination1.setForeground(new java.awt.Color(153, 153, 255));
        pagination1.setOpaque(false);
        jPanel3.add(pagination1);

        jLabel8.setText("Size:");

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

        cboMauSac.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel9.setText("Mô Tả");

        btnDeleteTab1.setText("Xóa");
        btnDeleteTab1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteTab1ActionPerformed(evt);
            }
        });

        jLabel4.setText("Hãng:");

        jLabel10.setText("Số Lượng");

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Tìm Kiếm Theo"));

        cboFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hãng", "Màu sắc", "Chất Liệu", "Size" }));
        cboFilter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboFilterItemStateChanged(evt);
            }
        });
        cboFilter.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                cboFilterPropertyChange(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cboFilter, 0, 144, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(cboFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbImage, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE))
                                .addGap(21, 21, 21)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(cboSize, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(cboChatLieu, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(cboSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(cboMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(cboHang, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnAddTab1)
                                .addGap(18, 18, 18)
                                .addComponent(btnUpdateTab1)
                                .addGap(18, 18, 18)
                                .addComponent(btnDeleteTab1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnClearTab1)))
                        .addGap(28, 28, 28))
                    .addComponent(jScrollPane2)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAddTab1, btnClearTab1, btnDeleteTab1, btnUpdateTab1});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(cboSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(cboMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(cboHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton4))
                                .addGap(11, 11, 11)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(cboChatLieu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(cboSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton3)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClearTab1)
                    .addComponent(btnDeleteTab1)
                    .addComponent(btnUpdateTab1)
                    .addComponent(btnAddTab1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void cboFilterPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_cboFilterPropertyChange
        // TODO add your handling code here:

    }//GEN-LAST:event_cboFilterPropertyChange

    private void cboFilterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboFilterItemStateChanged
        // TODO add your handling code here:
        searchEventSanPhamChiTiet();
    }//GEN-LAST:event_cboFilterItemStateChanged

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

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
        String textSearch = txtTimKiem.getText();
        List<SanPhamChiTiet> listAll = sanPhamChiTietService.selectAllByMaSanPham(GLOBAL_MA_SANPHAM);
        listSanPhamChiTiet.clear();
        for (SanPhamChiTiet sanPhamChiTiet : listAll) {
            SanPham sanPhamFound = sanPhamService.findById(sanPhamChiTiet.getMaSanPham());
            String tenSanPham = sanPhamFound.getTenSanPham();
            String tenHang = hangService.findById(sanPhamChiTiet.getMaHang()).getTenHang();
            String tenMauSac = mauSacService.findById(sanPhamChiTiet.getMaMauSac()).getTenMauSac();
            String tenChatLieu = chatLieuService.findById(sanPhamChiTiet.getMaChatLieu()).getTenChatLieu();
            String tenSize = sizeService.findById(sanPhamChiTiet.getMaSize()).getTenSize();
            String idSanPhamChiTiet = String.valueOf(sanPhamChiTiet.getMaSanPham());
            String cboFilterText = cboFilter.getSelectedItem().toString();

            System.out.println("Tìm Kiếm Theo: " + cboFilterText);

            if (cboFilterText.equalsIgnoreCase("Hãng") && (removeAccent(tenHang).contains(textSearch) == true || tenHang.contains(textSearch) == true)) {
                listSanPhamChiTiet.add(sanPhamChiTiet);
            }
            if (cboFilterText.equalsIgnoreCase("Màu sắc") && (removeAccent(tenMauSac).contains(textSearch) == true || tenMauSac.contains(textSearch) == true)) {
                listSanPhamChiTiet.add(sanPhamChiTiet);
            }
            if (cboFilterText.equalsIgnoreCase("Chất Liệu") && (removeAccent(tenChatLieu).contains(textSearch) == true || tenChatLieu.contains(textSearch) == true)) {
                listSanPhamChiTiet.add(sanPhamChiTiet);
            }
            if (cboFilterText.equalsIgnoreCase("Size") && (removeAccent(tenSize).contains(textSearch) == true || tenSize.contains(textSearch) == true)) {
                listSanPhamChiTiet.add(sanPhamChiTiet);
            }
        }
        addRowToTableByListSPCT(listSanPhamChiTiet);
    }//GEN-LAST:event_btnSearchActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddTab1;
    private javax.swing.JButton btnClearTab1;
    private javax.swing.JButton btnDeleteTab1;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnUpdateTab1;
    private javax.swing.JComboBox<String> cboChatLieu;
    private javax.swing.JComboBox<String> cboFilter;
    private javax.swing.JComboBox<String> cboHang;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
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
