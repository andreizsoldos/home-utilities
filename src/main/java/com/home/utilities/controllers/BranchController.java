package com.home.utilities.controllers;

import com.home.utilities.configuration.userdetails.UserPrincipal;
import com.home.utilities.entities.Branch;
import com.home.utilities.exceptions.NotFoundException;
import com.home.utilities.payload.dto.ClientCodeDetails;
import com.home.utilities.payload.request.ClientCodeRequest;
import com.home.utilities.payload.request.IndexRequest;
import com.home.utilities.services.ClientCodeService;
import com.home.utilities.services.IndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Controller
@RequiredArgsConstructor
public class BranchController {

    private static final String REDIRECT_USER_DASHBOARD = "redirect:/user/dashboard/";
    private static final String BRANCH = "branch";

    private final ClientCodeService clientCodeService;
    private final IndexService indexService;

    @GetMapping("/user/dashboard/{branch}")
    public ModelAndView displayBranchPage(@PathVariable(value = "branch") final String branch, final Locale locale) {
        final var mav = new ModelAndView(branch);
        final var branches = List.of(Branch.valueOf(branch.toUpperCase()));
        final var userId = UserPrincipal.getCurrentUser().getId();
        final var clientCodeList = clientCodeService.getClientCodes(branches, userId);
        final var firstClient = clientCodeService.findFirstClientCode(branches, userId).orElse(new ClientCodeDetails());
        final var indexList = indexService.getIndexes(Branch.valueOf(branch.toUpperCase()), userId);
        final var weekFirstDay = indexService.firstDayOfCurrentWeek();
        final var weekLastDay = indexService.lastDayOfCurrentWeek();
        final var weeklyStats = indexService.getIndexValueForCurrentWeek(Branch.valueOf(branch.toUpperCase()), userId, locale);
        final var monthFirstDay = indexService.firstDayOfCurrentMonth();
        final var monthLastDay = indexService.lastDayOfCurrentMonth();
        final var monthlyStats = indexService.getIndexValueForCurrentMonth(Branch.valueOf(branch.toUpperCase()), userId);
        final var lastCreatedIndexDate = indexService.getLastCreatedDate(Branch.valueOf(branch.toUpperCase()), userId);
        final var monthlyMinValues = indexService.getMonthlyMinIndexValues(Branch.valueOf(branch.toUpperCase()), userId, locale);
        final var monthlyMaxValues = indexService.getMonthlyMaxIndexValues(Branch.valueOf(branch.toUpperCase()), userId, locale);

        mav.addObject("clientCodeList", clientCodeList);
        mav.addObject("firstClient", firstClient);
        mav.addObject("indexList", indexList);
        mav.addObject("weekFirstDay", weekFirstDay);
        mav.addObject("weekLastDay", weekLastDay);
        mav.addObject("weeklyStats", weeklyStats);
        mav.addObject("monthFirstDay", monthFirstDay);
        mav.addObject("monthLastDay", monthLastDay);
        mav.addObject("monthlyStats", monthlyStats);
        mav.addObject("today", LocalDate.now());
        mav.addObject("lastCreatedIndexDate", lastCreatedIndexDate);
        mav.addObject("monthlyMinValues", monthlyMinValues);
        mav.addObject("monthlyMaxValues", monthlyMaxValues);
        return mav;
    }

    @GetMapping("/user/dashboard/{branch}/client-code")
    public ModelAndView displayClientCodePage(@PathVariable(value = "branch") final String branch) {
        final var mav = new ModelAndView("client-code");
        mav.addObject("clientCodeData", new ClientCodeRequest());
        mav.addObject(BRANCH, branch);
        return mav;
    }

    @PostMapping("/user/dashboard/{branch}/client-code")
    public String createClientCode(@Valid @ModelAttribute("clientCodeData") final ClientCodeRequest clientCodeRequest, final BindingResult bindingResult,
                                   @PathVariable(value = "branch") final String branch) {
        if (bindingResult.hasErrors()) {
            return "client-code";
        }
        final var userId = UserPrincipal.getCurrentUser().getId();
        return clientCodeService.createClientCode(clientCodeRequest, Branch.valueOf(branch.toUpperCase()), userId)
              .map(c -> REDIRECT_USER_DASHBOARD + branch + "#" + c.getId())
              .orElseThrow(() -> new NotFoundException("Client code", "number", clientCodeRequest.getClientNumber()));
    }

    @GetMapping("/user/dashboard/{branch}/client-code-edit/{clientId}")
    public ModelAndView displayClientCodeEditPage(@PathVariable(value = "branch") final String branch,
                                                  @PathVariable(value = "clientId") final Long clientId) {
        final var mav = new ModelAndView("client-code-edit");
        final var userId = UserPrincipal.getCurrentUser().getId();
        final var clientCode = clientCodeService.findByBranchAndClientIdAndUserId(Branch.valueOf(branch.toUpperCase()), clientId, userId);
        mav.addObject("clientCodeData", clientCode);
        mav.addObject(BRANCH, branch);
        return mav;
    }

    @PostMapping("/user/dashboard/{branch}/client-code/{clientId}")
    public String editClientCode(@Valid @ModelAttribute("clientCodeData") final ClientCodeRequest clientCodeRequest, final BindingResult bindingResult,
                                 @PathVariable(value = "branch") final String branch,
                                 @PathVariable(value = "clientId") final Long clientId) {
        if (bindingResult.hasErrors()) {
            return "client-code-edit";
        }
        final var userId = UserPrincipal.getCurrentUser().getId();
        return clientCodeService.editClientCode(clientCodeRequest, Branch.valueOf(branch.toUpperCase()), clientId, userId)
              .map(c -> REDIRECT_USER_DASHBOARD + branch)
              .orElseThrow(() -> new NotFoundException("Client code", "number", clientCodeRequest.getClientNumber()));
    }

    @ResponseBody
    @DeleteMapping("/user/dashboard/{branch}/client-code/{clientId}")
    public Integer deleteClientCode(@PathVariable(value = "branch") final String branch,
                                    @PathVariable(value = "clientId") final Long clientId) {
        final var userId = UserPrincipal.getCurrentUser().getId();
        return clientCodeService.deleteClientCode(Branch.valueOf(branch.toUpperCase()), clientId, userId);
    }

    @GetMapping("/user/dashboard/{branch}/client-code/{clientId}/client-index")
    public ModelAndView displayIndexPage(@PathVariable(value = "branch") final String branch,
                                         @PathVariable(value = "clientId") final Long clientId) {
        final var mav = new ModelAndView("client-index");
        final var userId = UserPrincipal.getCurrentUser().getId();
        final var lastIndex = indexService.getLastIndexValue(clientId, Branch.valueOf(branch.toUpperCase()), userId);
        mav.addObject("indexData", new IndexRequest(lastIndex.orElse(0D)));
        mav.addObject(BRANCH, branch);
        mav.addObject("clientId", clientId);
        return mav;
    }

    @PostMapping("/user/dashboard/{branch}/client-code/{clientId}/client-index")
    public String createIndex(@Valid @ModelAttribute("indexData") final IndexRequest indexRequest, final BindingResult bindingResult,
                              @PathVariable(value = "branch") final String branch,
                              @PathVariable(value = "clientId") final Long clientId) {
        if (bindingResult.hasErrors()) {
            return "client-index";
        }
        return indexService.createIndex(indexRequest, clientId)
              .map(c -> REDIRECT_USER_DASHBOARD + branch)
              .orElseThrow(() -> new NotFoundException("Index", "value", indexRequest.getValue()));
    }
}
