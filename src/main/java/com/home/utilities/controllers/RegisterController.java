package com.home.utilities.controllers;

import com.home.utilities.exceptions.EmailException;
import com.home.utilities.exceptions.NotFoundException;
import com.home.utilities.payload.request.RegisterRequest;
import com.home.utilities.services.ConfirmationTokenService;
import com.home.utilities.services.UserService;
import com.home.utilities.services.email.EmailService;
import com.home.utilities.services.util.DateTimeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private static final String HEADER_LOGO = "header_logo";
    private static final String FOOTER_LOGO = "footer_logo";

    private final UserService userService;
    private final EmailService emailService;
    private final ConfirmationTokenService confirmationTokenService;

    @GetMapping("/register")
    public ModelAndView displayRegisterPage() {
        return new ModelAndView("register", "userRegisterRequest", new RegisterRequest());
    }

    @PostMapping(value = "/register")
    public String postRegister(@Valid @ModelAttribute("userRegisterRequest") final RegisterRequest request,
                               final BindingResult bindingResult, final Locale locale) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        return userService.createAccount(request)
              .map(confirmationTokenService::generateToken)
              .map(confirmationToken -> emailService.sendEmail(request.getEmail(),
                    "account.activate.subject",
                    "email/account-activation-template",
                    Map.of("firstName", request.getFirstName().toUpperCase(),
                          "tokenValidity", DateTimeConverter.convertDateToString(confirmationToken.getExpiresAt(), DateTimeFormatter.ofPattern("HH:mm:ss, dd MMMM yyyy")),
                          "applicationAddress", emailService.getApplicationAddress(),
                          "token", confirmationToken.getToken()),
                    locale))
              .map(success -> "redirect:/login")
              .orElseThrow(() -> new EmailException("Error sending email to: ", request.getEmail()));
    }

    @GetMapping(value = "/register/account/activate/{token}")
    public String activateAccount(@PathVariable(name = "token") final String token, final Locale locale) {
        return Optional.of(token)
              .filter(confirmationTokenService::validateToken)
              .map(userService::activateAccount)
              .map(u -> emailService.sendEmail(u.getEmail(),
                    "account.activated.subject",
                    "email/account-activated-template",
                    Map.of("firstName", u.getFirstName().toUpperCase(),
                          "applicationAddress", emailService.getApplicationAddress()),
                    locale))
              .map(success -> "redirect:/login")
              .orElseThrow(() -> new NotFoundException("Token"));
    }
}
