package com.home.utilities.controller;

import com.home.utilities.entity.Gender;
import com.home.utilities.exception.EmailException;
import com.home.utilities.exception.NotFoundException;
import com.home.utilities.payload.request.SupportRequest;
import com.home.utilities.service.ConfirmationTokenService;
import com.home.utilities.service.UserService;
import com.home.utilities.service.email.EmailService;
import com.home.utilities.service.util.DateTimeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/support")
public class SupportController {

    private static final Integer REDIRECT_DURATION = 15; //seconds

    private final UserService userService;
    private final EmailService emailService;
    private final ConfirmationTokenService confirmationTokenService;

    @GetMapping
    public ModelAndView supportPage() {
        return new ModelAndView("support");
    }

    @GetMapping("/reset-password")
    public ModelAndView resetPasswordPage() {
        throw new NotFoundException("Reset password page not found");
        //return new ModelAndView("reset-password");
    }

    @GetMapping("/account/activate/link")
    public ModelAndView resendAccountActivationLinkPage() {
        return new ModelAndView("account-activation-link", "supportRequest", new SupportRequest());
    }

    @PostMapping(value = "/account/activate/link")
    public String postSendLink(@Valid @ModelAttribute("supportRequest") final SupportRequest request,
                               final BindingResult bindingResult, final Locale locale, final Model model) {
        if (bindingResult.hasErrors()) {
            return "account-activation-link";
        }
        return userService.checkAccount(request)
              .map(confirmationTokenService::generateToken)
              .map(confirmationToken -> emailService.sendEmail(request.getEmail(),
                    "account.activate.subject",
                    "email/account-activation-template",
                    Map.of("firstName", confirmationToken.getUser().getFirstName().toUpperCase(),
                          "tokenValidity", DateTimeConverter.convertDateToString(confirmationToken.getExpiresAt(), DateTimeFormatter.ofPattern("HH:mm:ss, dd MMMM yyyy")),
                          "applicationAddress", emailService.getApplicationAddress(),
                          "token", confirmationToken.getToken()),
                    locale))
              .map(success -> {
                  model.addAttribute("pageTitle", "support.account.title.link");
                  model.addAttribute("accountTitle", "support.account.title");
                  model.addAttribute("accountMessageTop", "support.account.message.top");
                  model.addAttribute("redirectDuration", REDIRECT_DURATION);
                  model.addAttribute("gender", userService.findByEmail(request.getEmail()).isPresent() ?
                        userService.findByEmail(request.getEmail()).get().getGender().name().toUpperCase() :
                        Gender.OTHER.name().toUpperCase());
                  return "account-created";
              })
              .orElseThrow(() -> new EmailException("Error sending email to: ", request.getEmail()));
    }
}
