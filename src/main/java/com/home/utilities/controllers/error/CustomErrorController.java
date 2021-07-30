package com.home.utilities.controllers.error;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Controller
public class CustomErrorController implements ErrorController {

    private static final String ERROR_MESSAGE = "errorMessage";

    @GetMapping("/error")
    public ModelAndView handleError(final HttpServletResponse response, final Exception ex) {
        final var statusCode = String.valueOf(response.getStatus());
        final var mav = new ModelAndView("error");

        final var codeIndex = IntStream.range(0, statusCode.length())
              .mapToObj(i -> "c" + i)
              .collect(toList());

        final var digits = statusCode.chars()
              .map(Character::getNumericValue)
              .mapToObj(d -> "" + d)
              .collect(toList());

        final var map = IntStream.range(0, codeIndex.size())
              .collect(HashMap<String, String>::new,
                    (m, i) -> m.put(codeIndex.get(i), digits.get(i)),
                    Map::putAll);

        if (statusCode.equals(String.valueOf(HttpStatus.BAD_REQUEST.value()))) {
            mav.addObject(ERROR_MESSAGE, "error.template.message.400");
        } else if (statusCode.equals(String.valueOf(HttpStatus.FORBIDDEN.value()))) {
            mav.addObject(ERROR_MESSAGE, "error.template.message.403");
        } else if (statusCode.equals(String.valueOf(HttpStatus.NOT_FOUND.value()))) {
            mav.addObject(ERROR_MESSAGE, "error.template.message.404");
        } else if (statusCode.equals(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))) {
            mav.addObject(ERROR_MESSAGE, "error.template.message.500");
        }
        mav.addAllObjects(map);
        return mav;
    }
}
