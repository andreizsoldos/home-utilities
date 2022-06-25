package com.home.utilities.configuration.email;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Properties;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class MailConfig {

    private static final String SMTP_HOST = "mail.smtp.host";
    private static final String SMTP_PORT = "mail.smtp.port";
    private static final String SMTP_USERNAME = "mail.username";
    private static final String SMTP_PASSWORD = "mail.password";
    private static final String SMTP_AUTH = "mail.smtp.auth";
    private static final String SMTP_STARTTLS = "mail.smtp.starttls.enable";
    private static final String SMTP_SSL = "mail.smtp.ssl.trust";

    private final Environment environment;

    @Bean
    public JavaMailSender mailSender() {
        final var javaMailProperties = new Properties();
        javaMailProperties.put(SMTP_HOST, "smtp.gmail.com");
        javaMailProperties.put(SMTP_PORT, "587");
        javaMailProperties.put(SMTP_AUTH, "true");
        javaMailProperties.put(SMTP_STARTTLS, "true");
        javaMailProperties.put(SMTP_SSL, "smtp.gmail.com");

        final var mailSender = new JavaMailSenderImpl();
        mailSender.setUsername(environment.getProperty(SMTP_USERNAME));
        mailSender.setPassword(environment.getProperty(SMTP_PASSWORD));
        mailSender.setJavaMailProperties(javaMailProperties);
        return mailSender;
    }

    @Bean
    public TemplateEngine emailTemplateEngine() {
        final var templateEngine = new SpringTemplateEngine();
        templateEngine.addDialect(new Java8TimeDialect());
        templateEngine.setTemplateResolver(htmlTemplateResolver());
        return templateEngine;
    }

    private ITemplateResolver htmlTemplateResolver() {
        final var templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setResolvablePatterns(Set.of("email/*", "fragments/email/*"));
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false);
        return templateResolver;
    }
}
