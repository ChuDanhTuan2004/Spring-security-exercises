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

    public void sendRegistrationApprovalEmail(String to, String username, String password) {
        String subject = "Thư Viện - Đăng Ký Thành Công";
        String htmlContent = String.format("""
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <h2>Xác Nhận Đăng Ký Tài Khoản Thư Viện</h2>
                <p>Kính gửi bạn,</p>
                <p>Yêu cầu đăng ký tài khoản thư viện của bạn đã được chấp nhận.</p>
                <p>Dưới đây là thông tin đăng nhập của bạn:</p>
                <div style="background-color: #f5f5f5; padding: 15px; border-radius: 5px;">
                    <p><strong>Tên đăng nhập:</strong> %s</p>
                    <p><strong>Mật khẩu:</strong> %s</p>
                </div>
                <p>Vui lòng đổi mật khẩu ngay sau khi đăng nhập lần đầu tiên.</p>
                <p>Trân trọng,<br/>Đội ngũ Thư viện</p>
            </body>
            </html>
            """, username, password);

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