package com.crawdwall_backend_api.utils.emailsenderservice;//package com.ghealead_backend_api.utils.emailsenderservice;
//
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//
//import java.util.Properties;
//
//@Configuration
//public class EmailConfig {
//
//    @Value("${email.sender.host}")
//    private String host;
//
//    @Value("${email.sender.port:587}")
//    private int port;
//
//    @Value("${email.sender.username}")
//    private String username;
//
//    @Value("${email.sender.password}")
//    private String password;
//
//    @Value("${email.sender.protocol:smtp}")
//    private String protocol;
//
//    @Value("${email.sender.auth:true}")
//    private boolean auth;
//
//    @Value("${email.sender.starttls.enable:true}")
//    private boolean starttls;
//
//    @Value("${email.debug:false}")
//    private boolean debug;
//
//    @Bean
//    public JavaMailSender javaMailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost(host);
//        mailSender.setPort(port);
//        mailSender.setUsername(username);
//        mailSender.setPassword(password);
//
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.transport.protocol", protocol);
//        props.put("mail.smtp.auth", auth);
//        props.put("mail.smtp.starttls.enable", starttls);
//        props.put("mail.debug", debug);
//
//        props.put("mail.smtp.connectiontimeout", 5000);
//        props.put("mail.smtp.timeout", 5000);
//        props.put("mail.smtp.writetimeout", 5000);
//
//        return mailSender;
//    }
//}