package dev.coffeebeanteam.spotifyshare.controller;

import dev.coffeebeanteam.spotifyshare.dto.ui.navbar.CollapsibleNavItem;
import dev.coffeebeanteam.spotifyshare.dto.ui.navbar.Divider;
import dev.coffeebeanteam.spotifyshare.dto.ui.navbar.NavItem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.ArrayList;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    @GetMapping
    public String index(Model model) {
        final ArrayList<Object> navBarItems = new ArrayList() {{
                add(new Divider());
                add(new NavItem()
                        .setTitle("Dashboard")
                        .setIconCss("fas fa-fw fa-tachometer-alt")
                );
                add(new CollapsibleNavItem()
                        .setTitle("Sharing")
                        .setSubTitle("Your Shares")
                        .setIconCss("")
                        .addNavItem("Top Artists and Tracks of Others", "#", "")
                        .addNavItem("Request Shares", "#", "")
                );
        }};

        model.addAttribute("pageTitle", "Dashboard");

        model.addAttribute("navBarItems", navBarItems);

        return "dashboard";
    }
}
