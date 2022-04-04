package com.home.utilities.services.email;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ThymeleafEmailBuilderImpl implements ThymeleafEmailBuilder {

    private static final String HEADER_LOGO = "headerLogo";

    private final TemplateEngine emailTemplateEngine;

    @Override
    public String build(final String emailTemplate, final Map<String, String> content, final Locale locale) {
        final var context = new Context(locale);
        content.forEach(context::setVariable);
        context.setVariables(Map.of(HEADER_LOGO, HEADER_LOGO));
        return emailTemplateEngine.process(emailTemplate, context);
    }
}
