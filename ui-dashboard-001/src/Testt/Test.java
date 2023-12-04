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
        
        Integer[] array = {1, 2, 3, 4, 2, 1, 5, 6, 3, 7, 8, 8};

        Set<Integer> set = new HashSet<>(Arrays.asList(array));

        Integer[] result = set.toArray(new Integer[0]);

        System.out.println(Arrays.toString(result));
        

     
    }
}
