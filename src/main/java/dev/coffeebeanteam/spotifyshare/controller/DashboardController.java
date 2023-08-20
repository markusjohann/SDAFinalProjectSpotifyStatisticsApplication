package dev.coffeebeanteam.spotifyshare.controller;

import dev.coffeebeanteam.spotifyshare.service.UserAccountService;
import dev.coffeebeanteam.spotifyshare.service.ui.NavBarService;
import dev.coffeebeanteam.spotifyshare.service.ui.TopItemsGalleryService;
import dev.coffeebeanteam.spotifyshare.service.ui.UserAccountDetailsService;
import org.apache.catalina.User;
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
    private TopItemsGalleryService topItemsGalleryService;

    private UserAccountService userAccountService;

    public DashboardController(
            NavBarService navBarService,
            UserAccountDetailsService userAccountDetailsService,
            TopItemsGalleryService topItemsGalleryService,
            UserAccountService userAccountService
    ) {
        this.navBarService = navBarService;
        this.userAccountDetailsService = userAccountDetailsService;
        this.topItemsGalleryService = topItemsGalleryService;
        this.userAccountService = userAccountService;
    }

    @GetMapping
    public String index(
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
            Model model
    ) {
        model
                .addAttribute("pageTitle", "Dashboard")
                .addAttribute("contentTitle", "Dashboard");

        userAccountService.setAuthorizedClient(authorizedClient);

        navBarService.populateViewModelWithNavBarItems(model);

        userAccountDetailsService.setAuthorizedClient(authorizedClient).populateViewModelWithUserDetails(model);

        topItemsGalleryService.setAuthorizedClient(authorizedClient).populateModelViewWithTopItems(model);

        return "default-page";
    }
}
