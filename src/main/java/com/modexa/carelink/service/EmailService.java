package com.modexa.carelink.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Async;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Value("${spring.mail.enabled:false}")
    private boolean mailEnabled;

    @Async
    public void sendVerificationEmail(String toEmail, String verificationToken) {
        if (!mailEnabled) {
            log.warn("Email service is disabled. Verification email not sent to: {}", toEmail);
            return;
        }

        try {
            log.info("Preparing to send verification email to: {}", toEmail);
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Verify your email address");

            String verificationLink = frontendUrl + "/verify-email?token=" + verificationToken;
            String emailContent = createVerificationEmailContent(verificationLink);

            helper.setText(emailContent, true);
            
            log.debug("Attempting to send email with verification link: {}", verificationLink);
            mailSender.send(message);
            log.info("Verification email sent successfully to: {}", toEmail);
        } catch (MessagingException e) {
            log.error("Failed to create verification email for: {}. Error: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to create verification email", e);
        } catch (MailException e) {
            log.error("Failed to send verification email to: {}. Error: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    private String createVerificationEmailContent(String verificationLink) {
        return String.format("""
            <html>
            <body style='font-family: Arial, sans-serif; line-height: 1.6; color: #333;'>
                <div style='max-width: 600px; margin: 0 auto; padding: 20px;'>
                    <h2 style='color: #2c3e50;'>Welcome to Carelink!</h2>
                    <p>Thank you for registering. Please verify your email address by clicking the button below:</p>
                    <div style='text-align: center; margin: 30px 0;'>
                        <a href='%s' 
                           style='background-color: #3498db; 
                                  color: white; 
                                  padding: 12px 24px; 
                                  text-decoration: none; 
                                  border-radius: 4px;
                                  display: inline-block;'>
                            Verify Email Address
                        </a>
                    </div>
                    <p>If the button doesn't work, you can also click on this link:</p>
                    <p><a href='%s'>%s</a></p>
                    <p>This verification link will expire in 24 hours.</p>
                    <p>If you didn't create an account, you can safely ignore this email.</p>
                    <hr style='border: none; border-top: 1px solid #eee; margin: 20px 0;'>
                    <p style='color: #666; font-size: 12px;'>
                        This is an automated message, please do not reply to this email.
                    </p>
                </div>
            </body>
            </html>
            """, verificationLink, verificationLink, verificationLink);
    }

    @Async
    public void sendPasswordResetEmail(String toEmail, String token) {
        if (!mailEnabled) {
            log.warn("Email service is disabled. Password reset token for {} is: {}", toEmail, token);
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Reset Your Password");

            String resetLink = frontendUrl + "/reset-password?token=" + token;
            String emailContent = createPasswordResetEmailContent(resetLink);

            helper.setText(emailContent, true);

            log.debug("Attempting to send email with password reset link: {}", resetLink);
            mailSender.send(message);
            log.info("Password reset email sent successfully to: {}", toEmail);
        } catch (MessagingException e) {
            log.error("Failed to create password reset email for: {}. Error: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to create password reset email", e);
        } catch (MailException e) {
            log.error("Failed to send password reset email to: {}. Error: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }

    private String createPasswordResetEmailContent(String resetLink) {
        return String.format("""
            <html>
            <body style='font-family: Arial, sans-serif; line-height: 1.6; color: #333;'>
                <div style='max-width: 600px; margin: 0 auto; padding: 20px;'>
                    <h2 style='color: #2c3e50;'>Reset Your Password</h2>
                    <p>Please click the button below to reset your password:</p>
                    <div style='text-align: center; margin: 30px 0;'>
                        <a href='%s' 
                           style='background-color: #3498db; 
                                  color: white; 
                                  padding: 12px 24px; 
                                  text-decoration: none; 
                                  border-radius: 4px;
                                  display: inline-block;'>
                            Reset Password
                        </a>
                    </div>
                    <p>If the button doesn't work, you can also click on this link:</p>
                    <p><a href='%s'>%s</a></p>
                    <p>This password reset link will expire in 24 hours.</p>
                    <p>If you didn't request a password reset, you can safely ignore this email.</p>
                    <hr style='border: none; border-top: 1px solid #eee; margin: 20px 0;'>
                    <p style='color: #666; font-size: 12px;'>
                        This is an automated message, please do not reply to this email.
                    </p>
                </div>
            </body>
            </html>
            """, resetLink, resetLink, resetLink);
    }
}
