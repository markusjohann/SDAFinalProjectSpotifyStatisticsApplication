package dev.coffeebeanteam.spotifyshare.controller;

import dev.coffeebeanteam.spotifyshare.dto.ui.UserAccountDto;
import dev.coffeebeanteam.spotifyshare.model.SharingStatus;
import dev.coffeebeanteam.spotifyshare.model.UserAccount;
import dev.coffeebeanteam.spotifyshare.model.UserAccountSharing;
import dev.coffeebeanteam.spotifyshare.model.UserAccountSharingKey;
import dev.coffeebeanteam.spotifyshare.repository.UserAccountRepository;
import dev.coffeebeanteam.spotifyshare.repository.UserAccountSharingRepository;
import dev.coffeebeanteam.spotifyshare.service.SharingService;
import dev.coffeebeanteam.spotifyshare.service.UserAccountService;
import dev.coffeebeanteam.spotifyshare.service.ui.NavBarService;
import dev.coffeebeanteam.spotifyshare.service.ui.TopItemsGalleryService;
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
    private UserAccountSharingRepository userAccountSharingRepository;

    private UserAccountService userAccountService;

    private TopItemsGalleryService topItemsGalleryService;

    public SharingController(
            NavBarService navBarService,
            UserAccountDetailsService userAccountDetailsService,
            SharingService sharingService,
            UserAccountService userAccountService,
            UserAccountRepository userAccountRepository,
            UserAccountSharingRepository userAccountSharingRepository,
            TopItemsGalleryService topItemsGalleryService
    ) {
        this.navBarService = navBarService;
        this.userAccountDetailsService = userAccountDetailsService;
        this.sharingService = sharingService;
        this.userAccountService = userAccountService;
        this.userAccountRepository = userAccountRepository;
        this.userAccountSharingRepository = userAccountSharingRepository;
        this.topItemsGalleryService = topItemsGalleryService;
    }

    @GetMapping("/view")
    public String sharingView(
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
            @RequestParam Long accountId,
            Model model
    ) {
        userAccountService.setAuthorizedClient(authorizedClient);

        final UserAccount loggedInUser = userAccountService.getLoggedInUserAccount();

        final UserAccount sharingUserAccount = userAccountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User account not found"));

        sharingService.getUserSharing(loggedInUser, sharingUserAccount)
                .ifPresentOrElse(
                        (sharing) -> {
                            final String sharerName = sharingUserAccount.getSpotifyUsername();

                            model
                                    .addAttribute("pageTitle", "Top Artists and Tracks of " + sharerName)
                                    .addAttribute("contentTitle", "Top Artists and Tracks of " + sharerName)
                                    .addAttribute("sharerName", sharerName)
                                    .addAttribute("sharerUserId", sharingUserAccount.getId());

                            navBarService.populateViewModelWithNavBarItems(model);

                            userAccountDetailsService.setAuthorizedClient(authorizedClient).populateViewModelWithUserDetails(model);

                            topItemsGalleryService.setAuthorizedClient(authorizedClient).populateModelViewWithTopItems(
                                    model,
                                    sharingUserAccount
                            );

                        },
                        () -> {
                            new ResponseStatusException(HttpStatus.NOT_FOUND, "Sharing between user accounts not found");
                        }
                );

        return "default-page";
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

        userAccountService.setAuthorizedClient(authorizedClient);

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

    @GetMapping("/request/accept-view")
    public String acceptRequestView(
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
            @RequestParam Long accountId,
            Model model
    ) {
        userAccountService.setAuthorizedClient(authorizedClient);

        final UserAccount loggedInUser = userAccountService.getLoggedInUserAccount();

        final UserAccountDto toAcceptUser = sharingService.getListOfToAcceptRequests(loggedInUser)
                .stream().filter(userAccountDto -> userAccountDto.getUserId() == accountId).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User account not found"));

        navBarService.populateViewModelWithNavBarItems(model);

        userAccountDetailsService.setAuthorizedClient(authorizedClient).populateViewModelWithUserDetails(model);

        model
                .addAttribute("pageTitle", "Accept or Reject a Sharing Request")
                .addAttribute("contentTitle", "Accept or Reject a Sharing Request")
                .addAttribute("acceptShareRequestUserAccountDto", toAcceptUser);


        return "default-page";
    }

    @GetMapping("/request/accept")
    public String acceptRequest(
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
            @RequestParam Long accountId,
            RedirectAttributes redirectAttributes
    ) {
        userAccountService.setAuthorizedClient(authorizedClient);

        final UserAccount toAccept = userAccountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User account not found"));

        sharingService.acceptSharingRequest(userAccountService.getLoggedInUserAccount(), toAccept);

        redirectAttributes.addFlashAttribute("successMessage", "Sharing request accepted!");

        return "redirect:/dashboard";
    }

    @GetMapping("/request/reject")
    public String rejectRequest(
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
            @RequestParam Long accountId,
            RedirectAttributes redirectAttributes
    ) {
        userAccountService.setAuthorizedClient(authorizedClient);

        final UserAccount toAccept = userAccountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User account not found"));

        sharingService.rejectSharingRequest(userAccountService.getLoggedInUserAccount(), toAccept);

        redirectAttributes.addFlashAttribute("successMessage", "Sharing request rejected!");

        return "redirect:/dashboard";
    }

    @GetMapping("/cancel")
    public String cancelSharing(
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
            @RequestParam Long accountId,
            RedirectAttributes redirectAttributes
    ) {

        userAccountService.setAuthorizedClient(authorizedClient);

        final UserAccount loggedInUser = userAccountService.getLoggedInUserAccount();

        final UserAccount toCancel = userAccountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User account not found"));

        sharingService.cancelUserSharing(loggedInUser, toCancel);

        redirectAttributes.addFlashAttribute("successMessage", "Sharing successfully canceled!");

        return "redirect:/dashboard";
    }
}
