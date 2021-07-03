package com.home.utilities.services.email;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ThymeleafEmailBuilderImpl implements ThymeleafEmailBuilder {

    private final TemplateEngine templateEngine;

    @Override
    public String build(final String emailTemplate, final Map<String, String> content) {
        final var context = new Context();
        content.forEach(context::setVariable);
        return templateEngine.process(emailTemplate, context);
    }
}
