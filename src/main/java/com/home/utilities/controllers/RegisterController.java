package com.home.utilities.controllers;

import com.home.utilities.exceptions.NotFoundException;
import com.home.utilities.payload.request.RegisterRequest;
import com.home.utilities.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final UserService userService;

    @GetMapping("/register")
    public ModelAndView displayRegisterPage() {
        return new ModelAndView("register", "userRegisterRequest", new RegisterRequest());
    }

    @PostMapping(value = "/register")
    public String postRegister(@Valid @ModelAttribute("userRegisterRequest") final RegisterRequest request, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        return userService.createAccount(request)
              .map(user -> "redirect:/login")
              .orElseThrow(() -> new NotFoundException("User", "email", request.getEmail()));
    }
}
