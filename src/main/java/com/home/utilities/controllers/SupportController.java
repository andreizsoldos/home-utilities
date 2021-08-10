package com.home.utilities.controllers;

import com.home.utilities.exceptions.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SupportController {

    @GetMapping("/support")
    public ModelAndView supportPage() {
        throw new NotFoundException("Support page not found");
        //return new ModelAndView("support");
    }
}
