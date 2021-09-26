package com.home.utilities.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

@ControllerAdvice
public class GlobalThymeleafVariablesController {

    @ModelAttribute("serverTimeZone")
    public String getServerTimeZone() {
        return displayTimeZoneOffset() + displayTimeZoneId();
    }

    private String displayTimeZoneOffset() {
        return "(GMT" + LocalDateTime.now().atZone(ZoneId.systemDefault()).getOffset().getId() + ") ";
    }

    private String displayTimeZoneId() {
        return TimeZone.getTimeZone(ZoneId.systemDefault()).getID();
    }
}
