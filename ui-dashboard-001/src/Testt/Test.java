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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.Normalizer;
import java.util.regex.Pattern;
/**
 *
 * @author phamh
 */
public class Test {
    public static void main(String[] args) {

        // Tạo một đối tượng DecimalFormat với mẫu số lẻ và chỉ định làm tròn lên 2 chữ số thập phân
        double number1 = 1.1;
        double number2 = 1.9;

        // Làm tròn lên
        int roundedNumber1 = (int) Math.ceil(number1);
        int roundedNumber2 = (int) Math.ceil(number2);

        // In kết quả
        System.out.println("Sau khi làm tròn " + number1 + ": " + roundedNumber1);
        System.out.println("Sau khi làm tròn " + number2 + ": " + roundedNumber2);
    }

     public static String removeDiacritics(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("").toLowerCase();
    }
}