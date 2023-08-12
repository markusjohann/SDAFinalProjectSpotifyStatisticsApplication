package dev.coffeebeanteam.spotifyshare.controller;

import dev.coffeebeanteam.spotifyshare.dto.ui.navbar.CollapsibleNavItem;
import dev.coffeebeanteam.spotifyshare.dto.ui.navbar.Divider;
import dev.coffeebeanteam.spotifyshare.dto.ui.navbar.Heading;
import dev.coffeebeanteam.spotifyshare.dto.ui.navbar.NavItem;
import dev.coffeebeanteam.spotifyshare.service.ui.NavBarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.ArrayList;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    private NavBarService navBarService;

    public DashboardController(NavBarService navBarService) {
        this.navBarService = navBarService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("pageTitle", "Dashboard");

        model.addAttribute("navBarItems", navBarService.getNavBarItems());

        return "dashboard";
    }
}
