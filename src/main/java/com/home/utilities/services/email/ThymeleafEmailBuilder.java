package com.home.utilities.services.email;

import java.util.Locale;
import java.util.Map;

public interface ThymeleafEmailBuilder {

    String build(String emailTemplate, Map<String, String> content, Locale locale);
}
