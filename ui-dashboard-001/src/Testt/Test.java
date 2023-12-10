/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Testt;

import com.raven.model.AddressDetails;
import com.raven.model.HeaderDetails;
import com.raven.model.Product;
import com.raven.model.ProductTableHeader;
import com.raven.service.CodingErrorPdfInvoiceCreator;
import java.io.File;
/**
 *
 * @author phamh
 */
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Test {

    public static void main(String[] args) throws FileNotFoundException {

        String filePath = ".....";

        // Tạo một đối tượng File với đường dẫn đã cung cấp
        File file = new File(filePath);

        // Kiểm tra xem tệp có tồn tại hay không
        if (file.exists()) {
            System.out.println("Tệp tồn tại.");
        } else {
            System.out.println("Tệp không tồn tại.");
        }
    }

  

}
