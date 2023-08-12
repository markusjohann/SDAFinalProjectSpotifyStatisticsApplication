package dev.coffeebeanteam.spotifyshare.controller;

import dev.coffeebeanteam.spotifyshare.service.ui.NavBarService;
import dev.coffeebeanteam.spotifyshare.service.ui.UserAccountDetailsService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    private NavBarService navBarService;
    private UserAccountDetailsService userAccountDetailsService;

    public DashboardController(
            NavBarService navBarService,
            UserAccountDetailsService userAccountDetailsService
    ) {
        this.navBarService = navBarService;
        this.userAccountDetailsService = userAccountDetailsService;
    }

    @GetMapping
    public String index(
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
            Model model
    ) {
        model.addAttribute("pageTitle", "Dashboard");

        navBarService.populateViewModelWithNavBarItems(model);

        userAccountDetailsService.setAuthorizedClient(authorizedClient).populateViewModelWithUserDetails(model);

        return "dashboard";
    }
}
