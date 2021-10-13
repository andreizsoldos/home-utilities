package com.home.utilities.controllers;

import com.home.utilities.configuration.userdetails.UserPrincipal;
import com.home.utilities.entities.Branch;
import com.home.utilities.services.ClientCodeService;
import com.home.utilities.services.IndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final IndexService indexService;
    private final ClientCodeService clientCodeService;

    @GetMapping("/user/dashboard")
    public ModelAndView dashboardPage() {
        final var mav = new ModelAndView("user-dashboard");
        final var userId = UserPrincipal.getCurrentUser().getId();
        List.of(Branch.values()).forEach(b -> {
            mav.addObject(b.name().toLowerCase() + "TotalClientCodes",
                  clientCodeService.getTotalClientCodes(b, userId));
            mav.addObject(b.name().toLowerCase() + "LastIndex",
                  indexService.getLastIndexValue(b, userId).orElse("0"));
            mav.addObject(b.name().toLowerCase() + "LastModifiedIndexDate",
                  indexService.getLastModifiedDate(b, userId).orElse("-"));
            mav.addObject(b.name().toLowerCase() + "ClientNameForLastIndex",
                  clientCodeService.getClientCodeNameWhoInsertedLastIndex(b, userId).orElse("-"));
            mav.addObject(b.name().toLowerCase() + "LastModificationDate",
                  indexService.getLastModificationDuration(b, userId));
        });
        return mav;
    }
}
