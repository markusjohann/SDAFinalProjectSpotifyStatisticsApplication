package dev.coffeebeanteam.spotifyshare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Index {
    @RequestMapping("/welcome")
    @ResponseBody
    public String welcome() {
        return "Welcome!";
    }
}
