/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Testt;

import com.raven.service.SanPhamService;
import com.ravent.database.DBContext;
import com.ravent.entity.SanPham;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author phamh
 */
public class Test {
    public static void main(String[] args) {
        String a = "Hãng Sản Phẩm";
        String result = removeAccent(a);
        System.out.println(result);
    }

    public static String removeAccent(String input) {
        String normalized = java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD);
        normalized = normalized.replaceAll("[^\\p{ASCII}]", "");
        return normalized.toLowerCase();
    }
}