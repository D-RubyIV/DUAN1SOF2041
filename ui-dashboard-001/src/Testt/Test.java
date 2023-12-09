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

        // Đặt ngày bắt đầu và ngày kết thúc
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 10);

        // Hiển thị các ngày trong khoảng
        displayDatesInRange(startDate, endDate);
    }

    private static void displayDatesInRange(LocalDate startDate, LocalDate endDate) {
        // Sử dụng vòng lặp for để duyệt qua các ngày trong khoảng
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            System.out.println(date);
        }
    }

}
