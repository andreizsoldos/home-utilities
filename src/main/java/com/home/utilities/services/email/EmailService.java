package com.home.utilities.services.email;

import java.util.List;
import java.util.Map;

public interface EmailService {

    boolean sendEmail(List<String> to, String subject, String body, String attachmentFilePath);

    boolean sendEmail(String to, String subject, String body, String attachmentFilePath);

    boolean sendEmail(List<String> to, String subject, String body);

    boolean sendEmail(String to, String subject, String body);

    boolean sendEmail(List<String> to, String subject, String emailTemplate, Map<String, String> content, String attachmentFilePath);

    boolean sendEmail(String to, String subject, String emailTemplate, Map<String, String> content, String attachmentFilePath);

    boolean sendEmail(List<String> to, String subject, String emailTemplate, Map<String, String> content);

    boolean sendEmail(String to, String subject, String emailTemplate, Map<String, String> content);

}
