package com.home.utilities.services;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.errors.MailjetClientRequestException;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.transactional.Attachment;
import com.mailjet.client.transactional.SendContact;
import com.mailjet.client.transactional.SendEmailsRequest;
import com.mailjet.client.transactional.TransactionalEmail;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final Environment environment;

    /**
     * Sends an email to a list of recipients
     *
     * @param emailTo            the list of recipients
     * @param receiverName       the name of receiver who will get the email
     * @param subject            the subject of the email
     * @param message            the body of the email
     * @param attachmentFilePath the file path for attachment
     * @return true if the email is successfully sent
     */
    @Override
    public boolean sendEmail(final List<String> emailTo, final String receiverName, final String subject, final String message, String attachmentFilePath) {
        final var fromEmail = environment.getProperty("mailjet.api.email.from");

        final Map<Boolean, List<String>> result = emailTo.stream()
              .collect(Collectors.partitioningBy(to -> sendEmail(buildMail(fromEmail, "Home Utilities", to, receiverName, subject, message, attachmentFilePath))));
        return result.containsKey(true);
    }

    /**
     * Sends an email to a single recipient
     *
     * @param emailTo            the recipient email address
     * @param receiverName       the name of who will receive the email
     * @param subject            the subject of the email
     * @param message            the body of the email
     * @param attachmentFilePath the file path for attachment
     * @return true if the email is successfully sent
     */
    @Override
    public boolean sendEmail(final String emailTo, final String receiverName, final String subject, final String message, String attachmentFilePath) {
        return sendEmail(List.of(emailTo), receiverName, subject, message, attachmentFilePath);
    }

    /**
     * Send the email message
     *
     * @param buildMail the object which builds the email
     * @return true if the email is sent successfully
     */
    private boolean sendEmail(final TransactionalEmail buildMail) {
        final var apiPublicKey = environment.getProperty("mailjet.api.publicKey");
        final var apiSecretKey = environment.getProperty("mailjet.api.secretKey");

        final var options = ClientOptions.builder()
              .apiKey(apiPublicKey)
              .apiSecretKey(apiSecretKey)
              .build();

        final var client = new MailjetClient(options);

        final var request = SendEmailsRequest.builder()
              .message(buildMail) // can be added up to 50 messages per request
              .build();

        new Thread(() -> {
            try {
                final var response = request.sendWith(client);
                Arrays.stream(response.getMessages())
                      .forEach(m -> LOGGER.debug("Email response status code: {}", m.getStatus()));
            } catch (MailjetClientRequestException e) {
                LOGGER.error("Failed to create email: {}", e.getMessage());
            } catch (MailjetException e) {
                LOGGER.error("Failed to send email: {}", e.getMessage());
            }
        }).start();

        return true;
    }

    /**
     * Sends an email to a single recipient
     *
     * @param emailFrom          the sender email address
     * @param senderName         the name of who will send the email
     * @param emailTo            the recipient email address
     * @param receiverName       the name of who will receive the email
     * @param subject            the subject of the email
     * @param message            the body of the email
     * @param attachmentFilePath the file path for attachment
     * @return true if the email is successfully sent
     */
    private TransactionalEmail buildMail(final String emailFrom, final String senderName, final String emailTo, final String receiverName,
                                         final String subject, final String message, String attachmentFilePath) {
        final var buildEmail = TransactionalEmail.builder()
              .from(new SendContact(emailFrom, senderName))
              .to(new SendContact(emailTo, receiverName))
              .subject(subject)
              .htmlPart(message);

        if (!attachmentFilePath.equals("")) {
            try {
                buildEmail.attachment(Attachment.fromFile(attachmentFilePath));
            } catch (IOException e) {
                LOGGER.error("Error reading the file: {}", e.getMessage());
            }
        }

        return buildEmail.build();
    }
}
