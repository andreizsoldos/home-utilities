package com.home.utilities.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.TimeZone;

@ControllerAdvice
public class GlobalThymeleafVariablesController {

    @ModelAttribute("serverTimeZone")
    public String getTimeZoneOffset() {
        return TimeZone.getDefault().getID();
    }
}
