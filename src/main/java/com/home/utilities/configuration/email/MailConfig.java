package com.home.utilities.configuration.email;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Objects;
import java.util.Properties;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class MailConfig {

    private static final String HOST = "mail.host";
    private static final String PORT = "mail.port";
    private static final String USERNAME = "mail.username";
    private static final String PASSWORD = "mail.password";
    private static final String SMTP_AUTH = "mail.smtp.auth";
    private static final String SMTP_STARTTLS = "mail.smtp.starttls.enable";
    private static final String SMTP_SSL = "mail.smtp.ssl.trust";
    private static final String SOCKET_FACTORY_PORT = "mail.smtp.socketFactory.port";
    private static final String SOCKET_FACTORY_CLASS = "mail.smtp.socketFactory.class";
    private static final String SOCKET_FACTORY_FALLBACK = "mail.smtp.socketFactory.fallback";

    private final Environment environment;

    @Bean
    public JavaMailSender mailSender() {
        final var mailSender = new JavaMailSenderImpl();
        mailSender.setHost(environment.getProperty(HOST));
        mailSender.setPort(Integer.parseInt(Objects.requireNonNull(environment.getProperty(PORT))));
        mailSender.setUsername(environment.getProperty(USERNAME));
        mailSender.setPassword(environment.getProperty(PASSWORD));

        final var javaMailProperties = new Properties();
        javaMailProperties.setProperty(SMTP_AUTH, environment.getProperty(SMTP_AUTH));
        javaMailProperties.setProperty(SMTP_STARTTLS, environment.getProperty(SMTP_STARTTLS));
        javaMailProperties.setProperty(SMTP_SSL, environment.getProperty(SMTP_SSL));
        javaMailProperties.setProperty(SOCKET_FACTORY_PORT, environment.getProperty(SOCKET_FACTORY_PORT));
        javaMailProperties.setProperty(SOCKET_FACTORY_CLASS, environment.getProperty(SOCKET_FACTORY_CLASS));
        javaMailProperties.setProperty(SOCKET_FACTORY_FALLBACK, environment.getProperty(SOCKET_FACTORY_FALLBACK));
        mailSender.setJavaMailProperties(javaMailProperties);
        return mailSender;
    }

    @Bean
    public TemplateEngine emailTemplateEngine() {
        final var templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(htmlTemplateResolver());
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
