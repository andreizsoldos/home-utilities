package com.home.utilities.services.email;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender javaMailSender;
    private final Environment environment;
    private final ThymeleafEmailBuilder thymeleafEmailBuilder;
    private final MessageSource messageSource;

    /**
     * Sends an email to a list of recipients with attachment
     *
     * @param to                 the list of recipients
     * @param subject            the subject of the email
     * @param body               the body of the email
     * @param attachmentFilePath the file path for attachment
     * @return true if the email is successfully sent
     */
    @Override
    public boolean sendEmail(final List<String> to, final String subject, final String body, final String attachmentFilePath) {

        final Map<Boolean, List<String>> result = to.stream()
              .collect(Collectors.partitioningBy(recipient -> sendMail(buildMail(recipient, subject, body, attachmentFilePath))));
        return result.containsKey(true);
    }

    /**
     * Sends an email to a single recipient with attachment
     *
     * @param to                 the recipient email address
     * @param subject            the subject of the email
     * @param body               the body of the email
     * @param attachmentFilePath the file path for attachment
     * @return true if the email is successfully sent
     */
    @Override
    public boolean sendEmail(final String to, final String subject, final String body, final String attachmentFilePath) {
        return this.sendEmail(List.of(to), subject, body, attachmentFilePath);
    }

    /**
     * Sends an email to a list of recipients without attachment
     *
     * @param to      the list of recipients
     * @param subject the subject of the email
     * @param body    the body of the email
     * @return true if the email is successfully sent
     */
    @Override
    public boolean sendEmail(final List<String> to, final String subject, final String body) {

        final Map<Boolean, List<String>> result = to.stream()
              .collect(Collectors.partitioningBy(recipient -> sendMail(buildMail(recipient, subject, body, ""))));
        return result.containsKey(true);
    }

    /**
     * Sends an email to a single recipient without attachment
     *
     * @param to      the recipient email address
     * @param subject the subject of the email
     * @param body    the body of the email
     * @return true if the email is successfully sent
     */
    @Override
    public boolean sendEmail(final String to, final String subject, final String body) {
        return this.sendEmail(List.of(to), subject, body, "");
    }

    /**
     * Sends an email with template to a list of recipients with attachment
     *
     * @param to                 the list of recipients
     * @param subject            the subject of the email as messageSource
     * @param emailTemplate      the thymeleaf template name to be included in mail
     * @param content            the content to be included in mail
     * @param attachmentFilePath the file path for attachment
     * @return true if the email is successfully sent
     */
    @Override
    public boolean sendEmail(final List<String> to, final String subject, final String emailTemplate, final Map<String, String> content, final String attachmentFilePath) {
        final var subjectMessage = messageSource.getMessage(subject, null, Locale.getDefault());
        final Map<Boolean, List<String>> result = to.stream()
              .collect(Collectors.partitioningBy(recipient -> sendMail(buildMail(recipient, subjectMessage, thymeleafEmailBuilder.build(emailTemplate, content), attachmentFilePath))));
        return result.containsKey(true);
    }

    /**
     * Sends an email with template to a single recipient with attachment
     *
     * @param to                 the list of recipients
     * @param subject            the subject of the email as messageSource
     * @param emailTemplate      the thymeleaf template name to be included in mail
     * @param content            the content to be included in mail
     * @param attachmentFilePath the file path for attachment
     * @return true if the email is successfully sent
     */
    @Override
    public boolean sendEmail(final String to, final String subject, final String emailTemplate, final Map<String, String> content, final String attachmentFilePath) {
        final var subjectMessage = messageSource.getMessage(subject, null, Locale.getDefault());
        return this.sendEmail(List.of(to), subjectMessage, emailTemplate, content, attachmentFilePath);
    }

    /**
     * Sends an email with template to a list of recipients without attachment
     *
     * @param to            the list of recipients
     * @param subject       the subject of the email as messageSource
     * @param emailTemplate the thymeleaf template name to be included in mail
     * @param content       the content to be included in mail
     * @return true if the email is successfully sent
     */
    @Override
    public boolean sendEmail(final List<String> to, final String subject, final String emailTemplate, final Map<String, String> content) {
        final var subjectMessage = messageSource.getMessage(subject, null, Locale.getDefault());
        final Map<Boolean, List<String>> result = to.stream()
              .collect(Collectors.partitioningBy(recipient -> sendMail(buildMail(recipient, subjectMessage, thymeleafEmailBuilder.build(emailTemplate, content), ""))));
        return result.containsKey(true);
    }

    /**
     * Sends an email with template to a single recipient without attachment
     *
     * @param to            the list of recipients
     * @param subject       the subject of the email as messageSource
     * @param emailTemplate the thymeleaf template name to be included in mail
     * @param content       the content to be included in mail
     * @return true if the email is successfully sent
     */
    @Override
    public boolean sendEmail(final String to, final String subject, final String emailTemplate, final Map<String, String> content) {
        final var subjectMessage = messageSource.getMessage(subject, null, Locale.getDefault());
        return this.sendEmail(List.of(to), subjectMessage, emailTemplate, content, "");
    }

    private boolean sendMail(final MimeMessage buildMail) {
        try {
            // Send the email asynchronously
            new Thread(() -> javaMailSender.send(buildMail)).start();
            return true;
        } catch (MailSendException e) {
            LOGGER.error("Failed to send email: {}", e.getMessage());
        } catch (MailException e) {
            LOGGER.error("Cannot send email: {}", e.getMessage());
        }
        return false;
    }

    private MimeMessage buildMail(final String to, final String subject, final String body, final String attachmentFilePath) {
        try {
            final var from = Objects.requireNonNull(environment.getProperty("spring.mail.username"));
            final var message = javaMailSender.createMimeMessage();
            final var helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            if (!attachmentFilePath.equals("")) {
                final var file = new FileSystemResource(new File(attachmentFilePath));
                helper.addAttachment("File", file);
            }

            return message;
        } catch (SendFailedException e) {
            LOGGER.error("Failed to send message: {}", e.getMessage());
            return null;
        } catch (MessagingException e) {
            LOGGER.error("Cannot send message: {}", e.getMessage());
            return null;
        }
    }
}
