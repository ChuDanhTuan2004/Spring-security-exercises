package com.example.jwtspring3.service.library.impl;


import com.example.jwtspring3.model.library.BookAccessPermission;
import com.fasterxml.jackson.core.JsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.auth.oauth2.GoogleCredentials;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.from}")
    private String fromEmail;

    @Value("${mail.personal}")
    private String personal;


    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, personal);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true indicates html

            mailSender.send(message);
            log.info("HTML email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send HTML email to: {}", to, e);
        }
    }

    public void sendRegistrationApprovalEmail(String to, String username) {
        String subject = "Thư viện Thành Đô - Đăng Ký Thành Công";
        String htmlContent = String.format("""
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
        </head>
        <body style="
            font-family: Arial, sans-serif;
            line-height: 1.6;
            color: #333;
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
            background-color: #f9f9f9;
        ">
            <div style="
                background-color: #ffffff;
                padding: 30px;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            ">
                <div style="text-align: center; margin-bottom: 30px;">
                    <h2 style="
                        color: #2c3e50;
                        margin: 0;
                        padding-bottom: 15px;
                        border-bottom: 2px solid #eee;
                    ">Xác Nhận Đăng Ký Tài Khoản Thư Viện</h2>
                </div>

                <p style="color: #555;">Kính gửi bạn,</p>
                
                <p style="color: #555;">
                    Chúc mừng! Yêu cầu đăng ký tài khoản thư viện Thành Đô của bạn đã được chấp nhận.
                </p>

                <div style="
                    background-color: #f8f9fa;
                    padding: 20px;
                    border-radius: 6px;
                    border-left: 4px solid #2c3e50;
                    margin: 20px 0;
                ">
                    <h3 style="
                        color: #2c3e50;
                        margin: 0 0 15px 0;
                        font-size: 16px;
                    ">Thông tin đăng nhập của bạn:</h3>
                    
                    <p style="margin: 8px 0;">
                        <strong style="color: #2c3e50;">Tên đăng nhập:</strong>
                        <span style="color: #555;">%s</span>
                    </p>
                    
                    <p style="margin: 8px 0;">
                        <strong style="color: #2c3e50;">Mật khẩu:</strong>
                        <span style="color: #555;">%s</span>
                    </p>
                </div>

                <div style="
                    background-color: #fff3f3;
                    border: 1px solid #dc3545;
                    color: #dc3545;
                    padding: 15px;
                    border-radius: 6px;
                    margin: 20px 0;
                    text-align: center;
                ">
                    <strong>⚠️ Lưu ý quan trọng:</strong>
                    <p style="margin: 8px 0;">
                        Vui lòng đổi mật khẩu ngay sau khi đăng nhập lần đầu tiên!
                    </p>
                </div>

                <div style="
                    margin-top: 30px;
                    padding-top: 20px;
                    border-top: 2px solid #eee;
                    color: #666;
                ">
                    <p style="margin: 0;">Trân trọng,</p>
                    <p style="margin: 5px 0;"><strong>Đội ngũ Thư viện</strong></p>
                </div>
            </div>
        </body>
        </html>
        """, username, "Password123!");

        sendHtmlEmail(to, subject, htmlContent);
    }

    public void sendRegistrationRejectionEmail(String to) {
        String subject = "Thư Viện - Đăng Ký Không Thành Công";
        String htmlContent = """
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <h2>Thông Báo Về Yêu Cầu Đăng Ký Tài Khoản</h2>
                <p>Kính gửi bạn,</p>
                <p>Chúng tôi rất tiếc phải thông báo rằng yêu cầu đăng ký tài khoản thư viện của bạn chưa được chấp nhận.</p>
                <p>Vui lòng liên hệ với nhân viên thư viện để biết thêm chi tiết.</p>
                <p>Trân trọng,<br/>Đội ngũ Thư viện</p>
            </body>
            </html>
            """;

        sendHtmlEmail(to, subject, htmlContent);
    }
}