package com.home.utilities.service.email;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface EmailService {

    boolean sendEmail(List<String> to, String subject, String emailTemplate, Map<String, String> content, String attachmentFilePath, Locale locale);

    boolean sendEmail(String to, String subject, String emailTemplate, Map<String, String> content, String attachmentFilePath, Locale locale);

    boolean sendEmail(List<String> to, String subject, String emailTemplate, Map<String, String> content, Locale locale);

    boolean sendEmail(String to, String subject, String emailTemplate, Map<String, String> content, Locale locale);

    String getApplicationAddress();
}
