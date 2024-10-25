package com.example.jwtspring3.config;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.Properties;

@Configuration
public class MailConfiguration {

    @Value("${mail.from}")
    private String from;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${spring.mail.password}")
    private String mailPassword;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true"); // Enabled for debugging, disable in production

        return mailSender;
    }

    @Bean
    public SimpleMailMessage templateSimpleMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        return message;
    }

    // For HTML emails
    @Bean
    public MimeMessageHelper mimeMessageHelper(JavaMailSender mailSender) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        return new MimeMessageHelper(mimeMessage, true, "UTF-8");
    }
}