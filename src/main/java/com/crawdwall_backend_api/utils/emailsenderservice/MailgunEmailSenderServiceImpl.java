package com.crawdwall_backend_api.utils.emailsenderservice;



import com.crawdwall_backend_api.urlshortener.UrlShortenerService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.util.Locale;

@Service("mailgunEmailSenderServiceImpl")
public class MailgunEmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final UrlShortenerService urlShortenerService;


    private final String year = String.valueOf(LocalDate.now().getYear());
    @Value("${application.configuration.organization.name}")
    private String organizationName;
    @Value("${application.configuration.organization.logo}")
    private String organizationLogo;
    @Value("${application.configuration.organization.supportEmail}")
    private String supportEmail;
    @Value("${application.configuration.organization.senderEmail}")
    private String senderEmail;
    @Value("${application.front-end-url}")
    private String frontEndUrl;
    @Value("${application.magik-link-base-url}")
    private String magikLinkBaseUrl;


    public MailgunEmailSenderServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine, UrlShortenerService urlShortenerService) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.urlShortenerService = urlShortenerService;

    }


    /**
     * Sends an email with the specified details using a configured mail sender.
     *
     * @param senderEmail    the email address of the sender
     * @param recipientEmail the email address of the recipient
     * @param subject        the subject line of the email
     * @param body           the content of the email
     * @throws MessagingException if there is an issue creating or sending the email
     */
    @Override
    public void sendEmail(String senderEmail, String recipientEmail, String subject, String body) {
        try {
            // Create a MimeMessage
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setTo(recipientEmail);
            helper.setSubject(subject);
            helper.setText(body, false);
            helper.setFrom(senderEmail);

            mailSender.send(message);
        } catch (MessagingException exception) {
            exception.printStackTrace();
        }
    }

    public void sendHtmlEmail(String senderEmail, String recipientEmail, String subject, String body) {
        try {
            // Create a MimeMessage
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(recipientEmail);
            helper.setSubject(subject);
            helper.setText(body, true);
            helper.setFrom(senderEmail);

            mailSender.send(message);

            System.out.println("I SENDING EMAIL TO: " + recipientEmail);
        } catch (MessagingException exception) {
//            log.info("Error, sending email");
        }
    }


    @Async
    public void sendUserResetPasswordEmail(String recipientEmail, String userId, String otp, String fullName) {


        String link = String.format(frontEndUrl + "/email-verification?email=" + recipientEmail + "&otp=" + otp);
        String shortenedUrl = String.valueOf(urlShortenerService.createMagicLink(link, recipientEmail, otp));
        String makicLink = magikLinkBaseUrl + shortenedUrl;

        Context context = new Context();
        context.setVariable("fullName", fullName);
        context.setVariable("organizationName", organizationName);
        context.setVariable("organizationLogo", organizationLogo);
        context.setVariable("link", makicLink);
        context.setVariable("year", year);
//        context.setVariable("otpExpiry", otpExpiry);
        // Process the HTML template with Thymeleaf
        String htmlBody = templateEngine.process("client-password-reset-template", context);
        sendHtmlEmail(senderEmail, recipientEmail, "PASSWORD RESET", htmlBody);

    }

    @Override
    @Async
    public void sendAccountActivationEmail(String emailAddress, String id, String fullName, String otp) {
        final String sender = "afsa-project@gozade.com";

        // Build activation URL (adjust base URL to your app)
        String link = String.format(frontEndUrl + "/email-verification?email=" + emailAddress + "&otp=" + otp);
        String shortenedUrl = String.valueOf(urlShortenerService.createMagicLink(link, emailAddress, otp));
        String makicLink = magikLinkBaseUrl + shortenedUrl;
        if (fullName == null) {
            fullName = "";
        }

        Context ctx = new Context(Locale.getDefault());
        ctx.setVariable("fullName", fullName);
        ctx.setVariable("otp", otp);
        ctx.setVariable("organizationName", "ALBION");
        ctx.setVariable("organizationLogo", organizationLogo); // ensure non-null
        ctx.setVariable("year", year);
        ctx.setVariable("verificationLink", makicLink);
        ctx.setVariable("supportEmail", "support@albion.com");

        // If your resolver uses suffix ".html", you can omit the extension here:
        String htmlBody = templateEngine.process("nominee-director-account-activation-template", ctx);

        sendHtmlEmail(sender, emailAddress, "ACCOUNT ACTIVATION", htmlBody);
    }

    @Override
    @Async
    public void sendPasswordChangeEmail(String emailAddress, String userId, String otp, String fullName) {


        Context context = new Context();
        context.setVariable("fullName", fullName);
        context.setVariable("organizationName", organizationName);
        context.setVariable("organizationLogo", organizationLogo);
        context.setVariable("otp", otp);
        context.setVariable("year", year);
        String htmlBody = templateEngine.process("nominee-director-password-change-template", context);
        sendHtmlEmail(senderEmail, emailAddress, "PASSWORD CHANGE", htmlBody);
    }

    @Override
    @Async
    public void sendPasswordResetEmail(String emailAddress, String userId, String otp, String fullName) {
        // Build activation URL (adjust base URL to your app)
        String link = String.format(frontEndUrl + "/reset-password?email=" + emailAddress + "&otp=" + otp);
        String shortenedUrl = String.valueOf(urlShortenerService.createMagicLink(link, emailAddress, otp));
        String makicLink = magikLinkBaseUrl + shortenedUrl;
        Context context = new Context();
        context.setVariable("fullName", fullName);
        context.setVariable("organizationName", organizationName);
        context.setVariable("organizationLogo", organizationLogo);

        context.setVariable("resetPasswordLink", makicLink);
        context.setVariable("year", year);
        String htmlBody = templateEngine.process("nominee-director-password-reset-template", context);
        sendHtmlEmail(senderEmail, emailAddress, "PASSWORD RESET", htmlBody);
    }

    @Override
    @Async
    public void sendAccountSetupCompletionEmail(String emailAddress, String fullName) {

        Context context = new Context();
        context.setVariable("fullName", fullName);
        context.setVariable("organizationName", organizationName);
        context.setVariable("organizationLogo", organizationLogo);
        context.setVariable("year", year);
        String htmlBody = templateEngine.process("nominee-account-setup-welcome-template", context);
        sendHtmlEmail(senderEmail, emailAddress, "ACCOUNT SETUP COMPLETION", htmlBody);
    }


    @Override
    @Async
    public void sendAdminAccountActivationEmail(String emailAddress, String fullName, String password, String otp) {
        final String sender = "afsa-project@gozade.com";
        String link = String.format("http://localhost:5173/admin/email-verification?email=" + emailAddress + "&otp=" + otp);
        String shortenedUrl = String.valueOf(urlShortenerService.createMagicLink(link, emailAddress, otp));
        String makicLink = magikLinkBaseUrl + shortenedUrl;
        Context ctx = new Context(Locale.getDefault());
        ctx.setVariable("fullName", fullName);
        ctx.setVariable("emailAddress", emailAddress);
        ctx.setVariable("password", password);
        ctx.setVariable("otp", otp);
        ctx.setVariable("organizationName", organizationName);
        ctx.setVariable("organizationLogo", organizationLogo);
        ctx.setVariable("supportEmail", supportEmail);
        ctx.setVariable("year", year);
        ctx.setVariable("verificationLink", makicLink);

        String htmlBody = templateEngine.process("admin-account-invitation-template", ctx);
        sendHtmlEmail(sender, emailAddress, "Admin Account Invitation - Your Credentials", htmlBody);
    }


    @Override
    @Async
    public void sendAdminPasswordResetEmail(String emailAddress, String userId, String otp, String fullName) {
        String link = String.format("http://localhost:5173/admin/reset-password?email=" + emailAddress + "&otp=" + otp);
        String shortenedUrl = String.valueOf(urlShortenerService.createMagicLink(link, emailAddress, otp));
        String magicLink = magikLinkBaseUrl + shortenedUrl;

        Context context = new Context();
        context.setVariable("fullName", fullName);
        context.setVariable("organizationName", organizationName);
        context.setVariable("organizationLogo", organizationLogo);
        context.setVariable("resetPasswordLink", magicLink);
        context.setVariable("supportEmail", supportEmail);
        context.setVariable("year", year);

        String htmlBody = templateEngine.process("admin-password-reset-template", context);
        sendHtmlEmail(senderEmail, emailAddress, "Reset Your Admin Password", htmlBody); // Better subject
    }


    @Override
    @Async
    public void sendCompanyAccountActivationEmail(String emailAddress, String fullName, String expiresAt, String otp) {
        final String sender = "afsa-project@gozade.com";
        String link = String.format(frontEndUrl + "/company/email-verification?email=" + emailAddress + "&otp=" + otp);
        String shortenedUrl = String.valueOf(urlShortenerService.createMagicLink(link, emailAddress, otp));
        String makicLink = magikLinkBaseUrl + shortenedUrl;
    

    Context ctx = new Context(Locale.getDefault());
        ctx.setVariable("fullName", fullName);
        ctx.setVariable("emailAddress", emailAddress);
        ctx.setVariable("expiresAt", expiresAt);
        ctx.setVariable("otp", otp);
        ctx.setVariable("organizationName", organizationName);
        ctx.setVariable("organizationLogo", organizationLogo);
        ctx.setVariable("supportEmail", supportEmail);
        ctx.setVariable("year", year);
        ctx.setVariable("verificationLink", makicLink);

        String htmlBody = templateEngine.process("company-account-activation-template", ctx);
        sendHtmlEmail(sender, emailAddress, "Company Account Activation", htmlBody);
    }

    @Override
    @Async
    public void sendCompanyPasswordResetEmail(String emailAddress, String userId, String otp, String fullName) {
        String link = String.format("http://localhost:5173/company/reset-password?email=" + emailAddress + "&otp=" + otp);
        String shortenedUrl = String.valueOf(urlShortenerService.createMagicLink(link, emailAddress, otp));
        String magicLink = magikLinkBaseUrl + shortenedUrl;
        
        Context context = new Context();
        context.setVariable("fullName", fullName);
        context.setVariable("organizationName", organizationName);
        context.setVariable("organizationLogo", organizationLogo);
        context.setVariable("resetPasswordLink", magicLink);
        context.setVariable("supportEmail", supportEmail);
        context.setVariable("year", year);
        
        String htmlBody = templateEngine.process("company-password-reset-template", context);
        sendHtmlEmail(senderEmail, emailAddress, "Reset Your Company Password", htmlBody);
    }
}
