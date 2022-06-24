package com.home.utilities.controller;

import com.home.utilities.exception.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ContactController {

    @GetMapping("/contact")
    public ModelAndView contactPage() {
        throw new NotFoundException("Contact page not found");
        //return new ModelAndView("contact");
    }
}
