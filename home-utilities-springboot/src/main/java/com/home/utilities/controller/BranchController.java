package com.home.utilities.controller;

import com.home.utilities.configuration.userdetails.UserPrincipal;
import com.home.utilities.entity.Branch;
import com.home.utilities.entity.ValueRange;
import com.home.utilities.exception.NotFoundException;
import com.home.utilities.payload.dto.ClientCodeDetails;
import com.home.utilities.payload.dto.IndexDetails;
import com.home.utilities.payload.request.ClientCodeRequest;
import com.home.utilities.payload.request.NewIndexRequest;
import com.home.utilities.payload.request.OldIndexRequest;
import com.home.utilities.service.ClientCodeService;
import com.home.utilities.service.IndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class BranchController {

    private static final String REDIRECT_USER_DASHBOARD = "redirect:/user/dashboard/";
    private static final String CLIENT_CODE_DATA = "clientCodeData";
    private static final String BRANCH = "branch";

    private final ClientCodeService clientCodeService;
    private final IndexService indexService;

    @GetMapping("/user/dashboard/{branch}")
    public ModelAndView displayBranchPage(@PathVariable(value = "branch") final String branch, final Locale locale) {
        final var mav = new ModelAndView(branch);
        final var branches = List.of(Branch.valueOf(branch.toUpperCase()));
        final var branchOrdinal = Branch.valueOf(branch.toUpperCase()).ordinal();
        final var userId = UserPrincipal.getCurrentUser().getId();
        final var firstClient = clientCodeService.findFirstClientCode(branches, userId).orElse(new ClientCodeDetails());
        final var indexList = indexService.getIndexes(Branch.valueOf(branch.toUpperCase()), userId);
        final var lastCreatedIndexDate = indexService.getLastCreatedDate(Branch.valueOf(branch.toUpperCase()), userId);
        final var clientCodeList = clientCodeService.getClientCodes(branches, userId);

        final var weekFirstDay = indexService.firstDayOfCurrentWeek();
        final var weekLastDay = indexService.lastDayOfCurrentWeek();
        final var daysOfWeek = indexService.currentDaysOfWeek();
        final var weeklyStats = clientCodeList.stream()
              .collect(Collectors.toMap(ClientCodeDetails::getId, c -> indexService.getIndexValuesForCurrentWeek(c.getId(), Branch.valueOf(branch.toUpperCase()), userId, locale)));
        final var previousWeekLastIndex = clientCodeList.stream()
              .collect(Collectors.toMap(ClientCodeDetails::getId, c -> indexService.getPreviousLastIndex(c.getId(), Branch.valueOf(branch.toUpperCase()), userId, weekFirstDay.minusDays(1L)).orElse(0D)));

        final var monthFirstDay = indexService.firstDayOfCurrentMonth();
        final var monthLastDay = indexService.lastDayOfCurrentMonth();
        final var monthlyStats = clientCodeList.stream()
              .collect(Collectors.toMap(ClientCodeDetails::getId, c -> indexService.getIndexValuesForCurrentMonth(c.getId(), Branch.valueOf(branch.toUpperCase()), userId)));
        final var previousMonthLastIndex = clientCodeList.stream()
              .collect(Collectors.toMap(ClientCodeDetails::getId, c -> indexService.getPreviousLastIndex(c.getId(), Branch.valueOf(branch.toUpperCase()), userId, monthFirstDay.minusDays(1L)).orElse(0D)));

        final var monthlyMinValues = clientCodeList.stream()
              .collect(Collectors.toMap(ClientCodeDetails::getId, c -> indexService.getMonthlyMinIndexValues(c.getId(), Branch.valueOf(branch.toUpperCase()), userId, locale)));
        final var monthlyMaxValues = clientCodeList.stream()
              .collect(Collectors.toMap(ClientCodeDetails::getId, c -> indexService.getMonthlyMaxIndexValues(c.getId(), Branch.valueOf(branch.toUpperCase()), userId, locale)));

        final var monthlyMinConsumptionValues = clientCodeList.stream()
              .collect(Collectors.toMap(ClientCodeDetails::getId, c -> indexService.getConsumptionValues(ValueRange.MIN, c.getId(), Branch.valueOf(branch.toUpperCase()), userId, locale)));
        final var monthlyMaxConsumptionValues = clientCodeList.stream()
              .collect(Collectors.toMap(ClientCodeDetails::getId, c -> indexService.getConsumptionValues(ValueRange.MAX, c.getId(), Branch.valueOf(branch.toUpperCase()), userId, locale)));
        final var yearlyStats = clientCodeList.stream()
              .collect(Collectors.toMap(ClientCodeDetails::getId, c -> indexService.getConsumptionValues(ValueRange.SUM, c.getId(), Branch.valueOf(branch.toUpperCase()), userId, locale)));

        final var indexIdList = indexList.stream()
              .map(IndexDetails::getId)
              .collect(Collectors.toList());
        final var oldIndexes = indexService.getOldIndexes(indexIdList);

        mav.addObject("branchOrdinal", branchOrdinal);
        mav.addObject("firstClient", firstClient);
        mav.addObject("indexList", indexList);
        mav.addObject("lastCreatedIndexDate", lastCreatedIndexDate);
        mav.addObject("clientCodeList", clientCodeList);
        mav.addObject("today", LocalDate.now(ZoneId.systemDefault()));
        mav.addObject("currentMonth", LocalDate.now(ZoneId.systemDefault()).getMonth().getDisplayName(TextStyle.FULL, locale).toUpperCase());
        mav.addObject("weekFirstDay", weekFirstDay);
        mav.addObject("weekLastDay", weekLastDay);
        mav.addObject("daysOfWeek", daysOfWeek);
        mav.addObject("previousWeekLastIndex", previousWeekLastIndex);
        mav.addObject("previousMonthLastIndex", previousMonthLastIndex);
        mav.addObject("weeklyStats", weeklyStats);
        mav.addObject("monthFirstDay", monthFirstDay);
        mav.addObject("monthLastDay", monthLastDay);
        mav.addObject("monthlyStats", monthlyStats);
        mav.addObject("monthlyMinValues", monthlyMinValues);
        mav.addObject("monthlyMaxValues", monthlyMaxValues);
        mav.addObject("yearlyStats", yearlyStats);
        mav.addObject("monthlyMinConsumptionValues", monthlyMinConsumptionValues);
        mav.addObject("monthlyMaxConsumptionValues", monthlyMaxConsumptionValues);
        mav.addObject("oldIndexes", oldIndexes);

        return mav;
    }

    @GetMapping("/user/dashboard/{branch}/client-code")
    public ModelAndView displayClientCodePage(@PathVariable(value = "branch") final String branch) {
        final var mav = new ModelAndView("client-code");
        mav.addObject(CLIENT_CODE_DATA, new ClientCodeRequest());
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
        mav.addObject(CLIENT_CODE_DATA, clientCode);
        mav.addObject(BRANCH, branch);
        return mav;
    }

    @PostMapping("/user/dashboard/{branch}/client-code-edit/{clientId}")
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
        mav.addObject("indexData", new NewIndexRequest(lastIndex.orElse(0D), clientId, Branch.valueOf(branch.toUpperCase())));
        mav.addObject(BRANCH, branch);
        mav.addObject("clientId", clientId);
        return mav;
    }

    @PostMapping("/user/dashboard/{branch}/client-code/{clientId}/client-index")
    public String createIndex(@Valid @ModelAttribute("indexData") final NewIndexRequest newIndexRequest, final BindingResult bindingResult,
                              @PathVariable(value = "branch") final String branch,
                              @PathVariable(value = "clientId") final Long clientId) {
        if (bindingResult.hasErrors()) {
            return "client-index";
        }
        return indexService.createIndex(newIndexRequest, clientId)
              .map(c -> REDIRECT_USER_DASHBOARD + branch)
              .orElseThrow(() -> new NotFoundException("Index", "value", newIndexRequest.getValue()));
    }

    @ResponseBody
    @PutMapping("/user/dashboard/{branch}/client-code/{clientId}/client-index/{indexId}")
    public String updateIndex(@Valid @RequestBody final OldIndexRequest oldIndexRequest, final BindingResult bindingResult,
                              @PathVariable(value = "branch") final String branch,
                              @PathVariable(value = "clientId") final Long clientId,
                              @PathVariable(value = "indexId") final Long indexId) {
        if (bindingResult.hasErrors()) {
            return bindingResult.getFieldErrors("value").stream()
                  .map(DefaultMessageSourceResolvable::getDefaultMessage)
                  .collect(Collectors.joining("<br>"));
        }
        final var userId = UserPrincipal.getCurrentUser().getId();
        indexService.saveOldIndex(oldIndexRequest, indexId);
        return indexService.updateIndex(oldIndexRequest, indexId)
              .filter(i -> i.getClientCode().getUser().getId().equals(userId))
              .filter(i -> i.getClientCode().getId().equals(clientId))
              .map(c -> "OK")
              .orElseThrow(() -> new NotFoundException("Index", "id", indexId));
    }
}
