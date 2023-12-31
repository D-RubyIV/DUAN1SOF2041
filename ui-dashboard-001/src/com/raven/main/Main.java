/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.raven.main;

import com.raven.entity.NguoiDung;
import com.raven.event.EventMenuSelected;
import com.raven.form.DialogDangNhap;
import com.raven.form.DialogThuocTinhSanPham;
import com.raven.form.Form_1;
import com.raven.form.Form_2;
import com.raven.form.Form_3;
import com.raven.form.Form_4;
import com.raven.form.Form_5;
import com.raven.form.Form_6;
import com.raven.form.Form_Home;
import com.raven.scroll.ScrollBarWin11UI;
import com.raven.service.NguoiDungService;
import com.raven.utils.Auth;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

/**
 *
 * @author phamh
 */
public class Main extends javax.swing.JFrame {

    /**
     * Creates new form NewJFrame1
     */
    private Form_Home home;
    private Form_1 form1;
    private Form_2 form2;
    private Form_3 form3;

    public Main() {

        // Fake Login
        NguoiDungService nguoiDungService = new NguoiDungService();
        NguoiDung nguoiDung = nguoiDungService.findById(11);
//        Auth.user = nguoiDung;

        if (Auth.user == null) {
            openLoginDialog();
        }

        this.setUndecorated(true);
        initFolder("/HonieSneacker/QrCode/Product");
        initFolder("/HonieSneacker/Bill");
        initComponents();
        setLocationRelativeTo(null);

        UIDefaults ui = UIManager.getDefaults();
        ui.put("ScrollBarUI", ScrollBarWin11UI.class.getCanonicalName());

        Form_6 form_6 = new Form_6();

        setBackground(new Color(0, 0, 0, 0));
        menu.initMoving(Main.this);
        menu.addEventMenuSelected(new EventMenuSelected() {
            @Override
            public void selected(int index) {
                System.out.println("Index: " + index);
                if (index == 0) {
                    setForm(new Form_5
        ());
                } else if (index == 1) {
                    setForm(new Form_Home());
                } else if (index == 2) {
                    if (Auth.user.getMaVaiTro() == 1) {
                        setForm(new Form_1());
                    } else {
                        JOptionPane.showMessageDialog(Main.this, "Bạn không có quyền truy cập");
                    }

                } else if (index == 3) {
                    setForm(new Form_2());
                } else if (index == 4) {
                    if (Auth.user.getMaVaiTro() == 1) {
                        setForm(new Form_3());
                    } else {
                        JOptionPane.showMessageDialog(Main.this, "Bạn không có quyền truy cập");
                    }
                } else if (index == 5) {
                    if (Auth.user.getMaVaiTro() == 1) {
                        setForm(new Form_4());
                    } else {
                        JOptionPane.showMessageDialog(Main.this, "Bạn không có quyền truy cập");
                    }

                } else if (index == 11) {
                    setForm(form_6);
                } else if (index == 12) {
                    Auth.user = null;
                    Main.this.setVisible(false);
                    openLoginDialog();
                    if (Auth.isLogin() == true) {
                        Main.this.setVisible(true);
                    }
                }
            }
        });

        //  set when system open start with home form
        setForm(new Form_5());
    }

    public void openLoginDialog() {
        DialogDangNhap dialogDangNhap = new DialogDangNhap(new javax.swing.JFrame(), true);
        dialogDangNhap.setVisible(true);
        dialogDangNhap.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("Dialog đã đóng");
            }
        });

        if (Auth.isLogin() != true) {
            System.exit(0);
        } else {
            if (!Auth.isManager() && !Auth.isStaff()) {
                JOptionPane.showMessageDialog(Main.this, "Bạn không có quyền truy cập");
                Auth.user = null;
                System.exit(0);
            }
        }
    }

    private void initFolder(String path) {
        Path duongDanThuMuc = Paths.get(path);

        if (!Files.exists(duongDanThuMuc)) {
            try {
                // Tạo thư mục nếu chưa tồn tại
                Files.createDirectories(duongDanThuMuc);
                System.out.println("Thư mục đã được tạo: " + duongDanThuMuc);
            } catch (IOException e) {
                System.err.println("Không thể tạo thư mục: " + e.getMessage());
            }
        } else {
            System.out.println("Thư mục đã tồn tại: " + duongDanThuMuc.toAbsolutePath());
        }
    }

    private void setForm(JComponent com) {
        mainPanel.removeAll();
        mainPanel.add(com);
        mainPanel.repaint();
        mainPanel.revalidate();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelBorder1 = new com.raven.swing.PanelBorder();
        menu = new com.raven.component.Menu();
        mainPanel = new javax.swing.JPanel();
        headerClose1 = new com.raven.component.HeaderClose();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelBorder1.setBackground(new java.awt.Color(242, 242, 242));

        mainPanel.setOpaque(false);
        mainPanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout panelBorder1Layout = new javax.swing.GroupLayout(panelBorder1);
        panelBorder1.setLayout(panelBorder1Layout);
        panelBorder1Layout.setHorizontalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addComponent(menu, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(headerClose1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1009, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        panelBorder1Layout.setVerticalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(menu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addComponent(headerClose1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBorder1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBorder1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.raven.component.HeaderClose headerClose1;
    private javax.swing.JPanel mainPanel;
    private com.raven.component.Menu menu;
    private com.raven.swing.PanelBorder panelBorder1;
    // End of variables declaration//GEN-END:variables
}
