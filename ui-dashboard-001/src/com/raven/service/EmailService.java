package com.raven.service;

import com.raven.model.ModelMessage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.*;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailService {

    public ModelMessage sendMail(List<String> listEmail, String messageString, String fileLocation) {
        ModelMessage ms = new ModelMessage(false, "");
        String from = "******@gmail.com";
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
        String username = "haanhhy01f@gmail.com";
        String password = "ffzc ials ccct zvpf";    //  Your email password here
//        String[] to = {"phamhaanh2k4.php@gmail.com", "anhphph36519@fpt.edu.vn", "recipient3@example.com"};

        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));

            InternetAddress[] toAddresses = new InternetAddress[listEmail.size()];
            for (int i = 0; i < listEmail.size(); i++) {
                toAddresses[i] = new InternetAddress(listEmail.get(i));
            }
            message.setRecipients(Message.RecipientType.TO, toAddresses);

            message.setSubject("HOINE SNEAKER SHOP EMAIL");
            message.setText(messageString);

            /////////////////////////
            File file = new File(fileLocation);
            System.out.println(fileLocation);
            if (file.exists() == true) {
                System.out.println("TON TAI FILE");
                
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(messageString);

                MimeBodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.attachFile(file);
                
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);
                multipart.addBodyPart(attachmentPart);
                message.setContent(multipart);
            }
            ////////////////////////

            Transport.send(message);

            System.out.println("Gửi email hoàn tất");
            ms.setMessage("Gửi email hoàn tất");

        } catch (MessagingException e) {
            if (e.getMessage().equals("Invalid Addresses")) {
                ms.setMessage("Email không hợp lệ");
            } else {
                System.out.println(e);
                ms.setMessage("Lỗi");
            }
        } catch (IOException ex) {
            System.out.println(ex);
            ms.setMessage("Lỗi");
        }
        System.out.println(ms);
        return ms;
    }
}
