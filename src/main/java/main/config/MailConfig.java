package main.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

  @Value("${spring.mail.host}")
  private String host;

  @Value("${spring.mail.port}")
  private Integer port;

  @Value("${spring.mail.properties.mail.transport.protocol}")
  private String mailTransportProtocol;

  @Value("${spring.mail.properties.smtp.auth}")
  private String mailSmtpAuth;

  @Value("${spring.mail.properties.smtp.starttls.enable}")
  private String mailStarttlsEnable;

  @Value("${spring.mail.properties.debug}")
  private String mailDebug;

  @Bean
  public JavaMailSender javaMailSender() {
    JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
    javaMailSender.setHost(host);
    javaMailSender.setPort(port);
    javaMailSender.setJavaMailProperties(getMailProperties());
    return javaMailSender;
  }

  private Properties getMailProperties() {
    Properties properties = new Properties();
    properties.setProperty("mail.transport.protocol", mailTransportProtocol);
    properties.setProperty("mail.smtp.auth", mailSmtpAuth);
    properties.setProperty("mail.smtp.starttls.enable", mailStarttlsEnable);
    properties.setProperty("mail.debug", mailDebug);
    return properties;
  }
}
