package dev.coffeebeanteam.spotifyshare.controller;

import dev.coffeebeanteam.spotifyshare.dto.ui.UserAccountDto;
import dev.coffeebeanteam.spotifyshare.model.UserAccount;
import dev.coffeebeanteam.spotifyshare.repository.UserAccountRepository;
import dev.coffeebeanteam.spotifyshare.service.SharingService;
import dev.coffeebeanteam.spotifyshare.service.UserAccountService;
import dev.coffeebeanteam.spotifyshare.service.ui.NavBarService;
import dev.coffeebeanteam.spotifyshare.service.ui.UserAccountDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/sharing")
public class SharingController {
    private NavBarService navBarService;
    private UserAccountDetailsService userAccountDetailsService;
    private UserAccountRepository userAccountRepository;
    private SharingService sharingService;

    private UserAccountService userAccountService;

    public SharingController(
            NavBarService navBarService,
            UserAccountDetailsService userAccountDetailsService,
            SharingService sharingService,
            UserAccountService userAccountService,
            UserAccountRepository userAccountRepository
    ) {
        this.navBarService = navBarService;
        this.userAccountDetailsService = userAccountDetailsService;
        this.sharingService = sharingService;
        this.userAccountService = userAccountService;
        this.userAccountRepository = userAccountRepository;
    }

    @GetMapping("/request")
    public String sharingRequestView(
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
            Model model
    ) {
        model
                .addAttribute("pageTitle", "Make a Sharing Request")
                .addAttribute("contentTitle", "Make a Sharing Request")
                .addAttribute("showSharingRequestForm", true);

        navBarService.populateViewModelWithNavBarItems(model);

        userAccountDetailsService.setAuthorizedClient(authorizedClient).populateViewModelWithUserDetails(model);

        return "default-page";
    }

    @GetMapping("/request/user-search")
    public ResponseEntity<List<UserAccountDto>> userSearch(
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
            @RequestParam String search
    ) {
        userAccountService.setAuthorizedClient(authorizedClient);

        final List<UserAccountDto> results = userAccountService.searchUsersByDisplayName(search);

        return ResponseEntity.ok(results);
    }

    @GetMapping("/request/make")
    public String makeRequest(
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
            @RequestParam Long receiverId,
            RedirectAttributes redirectAttributes
    ) {
        userAccountService.setAuthorizedClient(authorizedClient);

        final UserAccount requestReceiver = userAccountRepository.findById(receiverId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User account not found"));

        sharingService.requestSharing(userAccountService.getLoggedInUserAccount(), requestReceiver);

        redirectAttributes.addFlashAttribute("successMessage", "Sharing request made successfully!");

        return "redirect:/dashboard";
    }
}
