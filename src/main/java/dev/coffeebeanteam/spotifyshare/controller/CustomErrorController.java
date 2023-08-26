package dev.coffeebeanteam.spotifyshare.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {
    @RequestMapping("/error")
    public String error(HttpServletRequest request) {
        final int errorStatus =
                (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (errorStatus == 404) {
            return "errors/404";
        }

        return "errors/general";
    }
}

