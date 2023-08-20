package dev.coffeebeanteam.spotifyshare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/authorize/oauth2/code/spotify")
public class LogoutOnInvalidTokenController {
    @GetMapping
    public String index() {
        return "redirect:/logout";
    }
}
