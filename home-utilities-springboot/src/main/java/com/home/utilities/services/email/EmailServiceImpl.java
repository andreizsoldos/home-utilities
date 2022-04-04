package com.home.utilities.services.email;

import com.home.utilities.services.util.Translation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
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
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);
    private static final String CONTENT_ID_HEADER = "headerLogo";
    private static final String CONTENT_TYPE = "image/png";
    private static final String HEADER_LOGO_PATH = "static/images/header/logo2.png";

    private final JavaMailSender javaMailSender;
    private final Environment environment;
    private final Translation translation;
    private final ThymeleafEmailBuilder thymeleafEmailBuilder;

    /**
     * Sends an email with template to a list of recipients with attachment
     *
     * @param to                 the list of recipients
     * @param subject            the subject of the email as messageSource
     * @param emailTemplate      the thymeleaf template name to be included in mail
     * @param content            the content to be included in mail
     * @param attachmentFilePath the file path for attachment
     * @param locale             the locale language
     * @return true if the email is successfully sent
     */
    @Override
    public boolean sendEmail(final List<String> to, final String subject, final String emailTemplate, final Map<String, String> content, final String attachmentFilePath, final Locale locale) {
        final var subjectMessage = translation.getMessage(subject, null, locale);
        final Map<Boolean, List<String>> result = to.stream()
              .collect(Collectors.partitioningBy(recipient -> sendMail(buildMail(recipient, subjectMessage, thymeleafEmailBuilder.build(emailTemplate, content, locale), attachmentFilePath))));
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
     * @param locale             the locale language
     * @return true if the email is successfully sent
     */
    @Override
    public boolean sendEmail(final String to, final String subject, final String emailTemplate, final Map<String, String> content, final String attachmentFilePath, final Locale locale) {
        return this.sendEmail(List.of(to), subject, emailTemplate, content, attachmentFilePath, locale);
    }

    /**
     * Sends an email with template to a list of recipients without attachment
     *
     * @param to            the list of recipients
     * @param subject       the subject of the email as messageSource
     * @param emailTemplate the thymeleaf template name to be included in mail
     * @param content       the content to be included in mail
     * @param locale        the locale language
     * @return true if the email is successfully sent
     */
    @Override
    public boolean sendEmail(final List<String> to, final String subject, final String emailTemplate, final Map<String, String> content, final Locale locale) {
        final var subjectMessage = translation.getMessage(subject, null, locale);
        final Map<Boolean, List<String>> result = to.stream()
              .collect(Collectors.partitioningBy(recipient -> sendMail(buildMail(recipient, subjectMessage, thymeleafEmailBuilder.build(emailTemplate, content, locale), ""))));
        return result.containsKey(true);
    }

    /**
     * Sends an email with template to a single recipient without attachment
     *
     * @param to            the list of recipients
     * @param subject       the subject of the email as messageSource
     * @param emailTemplate the thymeleaf template name to be included in mail
     * @param content       the content to be included in mail
     * @param locale        the locale language
     * @return true if the email is successfully sent
     */
    @Override
    public boolean sendEmail(final String to, final String subject, final String emailTemplate, final Map<String, String> content, final Locale locale) {
        return this.sendEmail(List.of(to), subject, emailTemplate, content, locale);
    }

    /**
     * Get application URL address
     *
     * @return the URL address of application
     */
    @Override
    public String getApplicationAddress() {
        return environment.getProperty("application.address");
    }

    private boolean sendMail(final MimeMessage buildMail) {
        try {
            javaMailSender.send(buildMail);
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
            final var from = Objects.requireNonNull(environment.getProperty("mail.username"));
            final var message = javaMailSender.createMimeMessage();
            final var helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(from, "Home Utilities Platform");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            helper.addInline(CONTENT_ID_HEADER, new ClassPathResource(HEADER_LOGO_PATH), CONTENT_TYPE);

            if (!attachmentFilePath.equals("")) {
                final var file = new FileSystemResource(new File(attachmentFilePath));
                helper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
            }

            return message;
        } catch (SendFailedException e) {
            LOGGER.error("Failed to send message: {}", e.getMessage());
            return null;
        } catch (MessagingException e) {
            LOGGER.error("Cannot send message: {}", e.getMessage());
            return null;
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Cannot encode message: {}", e.getMessage());
            return null;
        }
    }
}
