/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raven.service;

import java.sql.*;
import com.raven.database.DBContext;
import com.raven.entity.ChatLieu;
import com.raven.entity.KhuyenMai;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
/**
 *
 * @author phamh
 */
public class ChatLieuService {

    public ChatLieu findById(int maChatLieu) {
        String sql = "SELECT * FROM CHATLIEU WHERE MACHATLIEU = " + maChatLieu;
//        System.out.println(sql);
        try {
            Statement statement = new DBContext().getConnect().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int maChat = rs.getInt("MACHATLIEU");
                String tenChat = rs.getString("TENCHATLIEU");
                ChatLieu chatLieu = new ChatLieu(maChat, tenChat);
                return chatLieu;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<ChatLieu> selectAll() {
        List<ChatLieu> listChatLieu = new ArrayList<>();
        String sql = "SELECT * FROM CHATLIEU";
        try {
            Statement st = new DBContext().getConnect().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                int maChat = rs.getInt("MACHATLIEU");
                String tenChat = rs.getString("TENCHATLIEU");
                listChatLieu.add(new ChatLieu(maChat, tenChat));
            }
        } catch (Exception e) {
            System.out.println("CHATLIEU SERVICE ERROR SELECT ALL:" + e);
        }
        return listChatLieu;
    }

    public String add(String tenChatLieu) {
        Connection con = new DBContext().getConnect();
        String sql = "INSERT INTO CHATLIEU (TENCHATLIEU) VALUES (?)";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, tenChatLieu);
            int result = st.executeUpdate();
            if (result > 0) {
                return "Thêm Thành Công";
            }
            return "Thêm Thất Bại";
        } catch (Exception e) {
            return "Thêm Lỗi: " + e;
        }
    }

    public String delete(int maChatLieu) {
        Connection con = new DBContext().getConnect();
        String sql = "DELETE FROM CHATLIEU WHERE MACHATLIEU = ?";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, maChatLieu);
            int result = st.executeUpdate();
            if (result > 0) {
                return "Xóa Thành Công";
            }
            return "Xóa Thất Bại";
        } catch (Exception e) {
            if(e.toString().contains("FK")){
                return "Xóa Lỗi: Tồn Tại Khóa Ngoại";
            }
            return "Xóa Lỗi: " + e;
        }
    }

    public String update(ChatLieu chatLieu) {
        Connection con = new DBContext().getConnect();
        String sql = "UPDATE CHATLIEU SET TENCHATLIEU=? WHERE MACHATLIEU = ?";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, chatLieu.getTenChatLieu());
            st.setInt(2, chatLieu.getMaChatLieu());

            int result = st.executeUpdate();
            if (result > 0) {
                return "Update Thành Công";
            }
            return "Update Thất Bại";
        } catch (Exception e) {
            return "Update Lỗi: " + e;
        }
    }
}