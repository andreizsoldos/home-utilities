package com.home.utilities.services;

import java.util.List;

public interface EmailService {

    boolean sendEmail(List<String> emailTo, String receiverName, String subject, String body, String attachmentFilePath);

    boolean sendEmail(String emailTo, String receiverName, String subject, String body, String attachmentFilePath);
}
