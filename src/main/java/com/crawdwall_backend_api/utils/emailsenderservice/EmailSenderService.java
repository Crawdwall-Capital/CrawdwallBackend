package com.crawdwall_backend_api.utils.emailsenderservice;


public interface EmailSenderService {


    void sendHtmlEmail(String senderEmail, String recipientEmail,String subject, String body);

    void sendEmail(String senderEmail, String recipientEmail, String subject, String body);

    void sendAccountActivationEmail(String emailAddress, String id, String fullName, String otp);
    void sendPasswordChangeEmail(String emailAddress, String userId, String otp, String fullName);
    void sendPasswordResetEmail(String emailAddress, String userId, String otp, String fullName);
    void sendAdminPasswordResetEmail(String emailAddress, String userId, String otp, String fullName);
    void sendAccountSetupCompletionEmail(String emailAddress, String s);

    void sendAdminAccountActivationEmail(String emailAddress, String fullName, String password, String otp);
    void sendCompanyAccountActivationEmail(String emailAddress, String fullName, String expiresAt, String otp);
    void sendCompanyPasswordResetEmail(String emailAddress, String userId, String otp, String fullName);
}
