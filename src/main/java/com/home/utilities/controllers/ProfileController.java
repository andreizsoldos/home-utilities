package com.home.utilities.controllers;

import com.home.utilities.exceptions.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProfileController {

    @GetMapping("/user/profile")
    public ModelAndView userProfilePage() {
        throw new NotFoundException("User profile page not found");
        //return new ModelAndView("user-profile");
    }
}
