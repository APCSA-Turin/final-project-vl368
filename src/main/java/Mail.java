package com.example;

import java.util.*;

import jakarta.mail.*;
import jakarta.mail.internet.*;

// https://javaee.github.io/javamail/docs/api/
// used code from api as base
// trying to tweak it to send an email
// but i cant find the correct dependency to put into maven

public class Mail {
    private static String emailAddress = "pricetracker22564@gmail.com";
    private static String password = "hozzar-1bepvy-nEhmac";

    
    public static void sendMail(String userEmail, Product p) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getInstance(props,
            new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailAddress, password);
                }
            });
        String emailText = "Your item: " + p.getName()
                            + " has changed prices. \n"
                            + "It has dropped " 
                            + p.getPriceDrop() + "%\n"
                            + "From " + p.getOriginal() 
                            + " to " + p.getNewest() + ".\n";
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(emailAddress);
            msg.setRecipients(Message.RecipientType.TO,
                            userEmail);
            msg.setSubject("Recent price change");
            msg.setSentDate(new Date());
            msg.setText(emailText);
            Transport.send(msg, emailAddress, password);
        } catch (MessagingException mex) {
            System.out.println("send failed, exception: " + mex);
        }
    }
}
