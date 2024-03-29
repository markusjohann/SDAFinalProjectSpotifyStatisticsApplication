package dev.coffeebeanteam.spotifyshare.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String to, String subject, String body) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("info@pjohanson.dev");
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(body);

        javaMailSender.send(mail);
    }
}
