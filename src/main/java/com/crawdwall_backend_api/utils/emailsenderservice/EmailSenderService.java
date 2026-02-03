package com.crawdwall_backend_api.utils.emailsenderservice;


public interface EmailSenderService {


    void sendHtmlEmail(String senderEmail, String recipientEmail,String subject, String body);

    void sendEmail(String senderEmail, String recipientEmail, String subject, String body);

    void appUserAccountActivationEmail(String emailAddress, Long id, String otp, String expiresAt);

    void adminAccountActivationEmail(String emailAddress, String firstName, String lastName, String password, String expiresAt, String otp);


    void appUserResetPasswordEmail(String otp, String expireAt, String emailAddresss);

    void sendAppUserPasswordChangeEmail(String emailAddress);

    void sendCompanyAccountActivationEmail(String emailAddress, String firstName, String expiresAt, String otp);

   }
