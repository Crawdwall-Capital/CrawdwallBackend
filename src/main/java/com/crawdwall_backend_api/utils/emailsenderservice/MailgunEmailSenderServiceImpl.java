package com.crawdwall_backend_api.utils.emailsenderservice;//package com.crawdwall_api_application.utils.emailsenderservice;
//
//
//
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//import org.thymeleaf.TemplateEngine;
//import org.thymeleaf.context.Context;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Locale;
//
//@Service("mailgunEmailSenderServiceImpl")
//@Slf4j
//@RequiredArgsConstructor
//public class MailgunEmailSenderServiceImpl implements EmailSenderService {
//
//
//    private final JavaMailSender javaMailSender;
//    private final TemplateEngine templateEngine;
//    String senderEmail = "no-reply@ghealead.com";
//    @Value("${spring.application.backend-accept-admin-invite-base-url}")
//    private String backendAcceptAdminInviteBaseUrl;
//
//
//
//    private final String year = String.valueOf(LocalDate.now().getYear());
//
//
//
//
////    public MailgunEmailSenderServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
////        this.mailSender = mailSender;
////        this.templateEngine = templateEngine;
////
////
////    }
//
//
//
//
//
//
//    /**
//     * Sends an email with the specified details using a configured mail sender.
//     *
//     * @param senderEmail    the email address of the sender
//     * @param recipientEmail the email address of the recipient
//     * @param subject        the subject line of the email
//     * @param body           the content of the email
//     * @throws MessagingException if there is an issue creating or sending the email
//     */
//    @Async
//    @Override
//    public void sendEmail(String senderEmail, String recipientEmail, String subject, String body) {
//        try {
//        // Create a MimeMessage
//        MimeMessage message = javaMailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
//        helper.setTo(recipientEmail);
//        helper.setSubject(subject);
//        helper.setText(body, false);
//            helper.setFrom(senderEmail);
//
//            javaMailSender.send(message);
//        } catch (MessagingException exception) {
//            exception.printStackTrace();
//        }
//    }
//    @Async
//    @Override
//    public void appUserAccountActivationEmail(String emailAddress, Long id, String otp, String expiresAt) {
//        try {
// // Parse the full date string with the correct format
//            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(
//                "EEEE, MMMM d, yyyy 'at' h:mm a",
//                Locale.US
//            );
//            LocalDateTime expiryTime = LocalDateTime.parse(expiresAt, inputFormatter);
//
//            // Format to just time (e.g., "4:41 PM")
//            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
//            String expiryTimeOnly = expiryTime.format(timeFormatter);
//
//            Context context = new Context();
//            context.setVariable("otpCode", otp);
//            context.setVariable("expiryTime", expiryTimeOnly);
//            context.setVariable("currentYear", LocalDate.now().getYear());
//            context.setVariable("unsubscribeUrl", "unsubscribeUrl.com");
//            context.setVariable("privacyUrl", "privacyUrl.com");
//            // Process the HTML template with Thymeleaf
//            String htmlBody = templateEngine.process("app-user-onboarding-email-template", context);
//
//            sendHtmlEmail(senderEmail, emailAddress, "Email Verification", htmlBody);
//            log.info("App user account activation email sent successfully to: {}", emailAddress);
//        } catch (Exception e) {
//            log.error("Error sending app user account activation email: {}", e.getMessage());
//        }
//    }
//
//    @Async
//    public void sendHtmlEmail(String senderEmail, String recipientEmail, String subject, String body) {
//        try {
//            // Create a MimeMessage
//            MimeMessage message = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//            helper.setTo(recipientEmail);
//            helper.setSubject(subject);
//            helper.setText(body, true);
//            helper.setFrom(senderEmail);
//
//            javaMailSender.send(message);
//
//            System.out.println("I SENDING EMAIL TO: " + recipientEmail);
//        } catch (MessagingException exception) {
//            log.error("Error sending email: {}", exception.getMessage());
////            log.info("Error, sending email");
//        }
//    }
//    @Async
//    @Override
//    public void adminAccountActivationEmail(String emailAddress, String firstName, String lastName, String password, String expiresAt, String otp) {
//        try {
//          // Parse the full date string with the correct format
//          DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(
//            "EEEE, MMMM d, yyyy 'at' h:mm a",
//            Locale.US
//    );
//    LocalDateTime expiryTime = LocalDateTime.parse(expiresAt, inputFormatter);
//
//    // Format to just time (e.g., "4:41 PM")
//    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
//    String expiryTimeOnly = expiryTime.format(timeFormatter);
//    String acceptInviteUrl = backendAcceptAdminInviteBaseUrl + "/api/v1/admin/public/accept-invite/" + otp+"/"+emailAddress;
//        Context context = new Context();
//        context.setVariable("firstName", firstName+" "+lastName);
//        context.setVariable("password", password);
//        context.setVariable("emailAddress", emailAddress);
//        context.setVariable("expiryTime", expiryTimeOnly);
//        context.setVariable("currentYear", LocalDate.now().getYear());
//        context.setVariable("acceptInviteUrl", acceptInviteUrl);
//            String htmlBody = templateEngine.process("admin-account-activation-email-template", context);
//            sendHtmlEmail(senderEmail, emailAddress, "Ghealead Admin Invitation", htmlBody);
//            log.info("Admin account activation email sent successfully to: {}", emailAddress);
//        } catch (Exception e) {
//            log.error("Error sending admin account activation email: {}", e.getMessage());
//        }
//    }
//
//    @Override
//    public void appUserResetPasswordEmail(String otp, String expireAt, String emailAddress) {
//        try {
//           // Parse the full date string with the correct format
//           DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(
//            "EEEE, MMMM d, yyyy 'at' h:mm a",
//            Locale.US
//    );
//    LocalDateTime expiryTime = LocalDateTime.parse(expireAt, inputFormatter);
//
//    // Format to just time (e.g., "4:41 PM")
//    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
//    String expiryTimeOnly = expiryTime.format(timeFormatter);
//            Context context = new Context();
//
//            context.setVariable("expiryTime", expiryTimeOnly);
//            context.setVariable("otpCode", otp);
//            context.setVariable("currentYear", LocalDate.now().getYear());
//            String htmlBody = templateEngine.process("app-user-reset-password", context);
//            sendHtmlEmail(senderEmail, emailAddress, "Account Update", htmlBody);
//            log.info("reset password email sent successfully to: {}", emailAddress);
//        } catch (Exception e) {
//            log.error("Error sending reset password email: {}", e.getMessage());
//        }
//    }
//
//    @Override
//    public void sendAppUserPasswordChangeEmail(String emailAddress) {
//
//        try {
//            Context context = new Context();
//            context.setVariable("currentYear", LocalDate.now().getYear());
//            String htmlBody = templateEngine.process("app-user-complete-password-change-email-template", context);
//            sendHtmlEmail(senderEmail, emailAddress, "Password Changed Successfully", htmlBody);
//            log.info("App user password changed successfully email sent successfully to: {}", emailAddress);
//        } catch (Exception e) {
//            log.error("Error sending app user password changed successfully email: {}", e.getMessage());
//        }
//
//    }
//
//
//
//
//}