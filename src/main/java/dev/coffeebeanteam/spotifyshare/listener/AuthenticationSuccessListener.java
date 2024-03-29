package dev.coffeebeanteam.spotifyshare.listener;

import dev.coffeebeanteam.spotifyshare.dto.UserDto;
import dev.coffeebeanteam.spotifyshare.dto.TopItemsResponseDto;
import dev.coffeebeanteam.spotifyshare.model.UserAccount;
import dev.coffeebeanteam.spotifyshare.repository.UserAccountRepository;
import dev.coffeebeanteam.spotifyshare.service.EmailService;
import dev.coffeebeanteam.spotifyshare.service.SpotifyApiService;
import dev.coffeebeanteam.spotifyshare.service.UserTopItemsService;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final UserAccountRepository userRepository;
    private final SpotifyApiService spotifyApiService;
    private final UserTopItemsService userTopItemsService;
    private final EmailService emailService;

    public AuthenticationSuccessListener(
            UserAccountRepository userRepository,
            SpotifyApiService spotifyApiService,
            UserTopItemsService userTopItemsService,
            EmailService emailService
    ) {
        this.userRepository = userRepository;
        this.spotifyApiService = spotifyApiService;
        this.userTopItemsService = userTopItemsService;
        this.emailService = emailService;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        if (event.getSource() instanceof OAuth2LoginAuthenticationToken) {
            final OAuth2LoginAuthenticationToken authentication = (OAuth2LoginAuthenticationToken)event.getSource();

            final String principalName = authentication.getPrincipal().getName();
            final String displayName = (String) authentication.getPrincipal().getAttributes().get("display_name");
            final String token = authentication.getAccessToken().getTokenValue();

            spotifyApiService.setToken(token);
            final UserDto spotifyUser = spotifyApiService.getUser();

            try {
                handleTopItemsSync(
                        spotifyUser,
                        principalName,
                        displayName
                );
            } catch (WebClientResponseException e) {
                if (e.getStatusCode() == HttpStatusCode.valueOf(403)) {
                    handleUnapprovedUser(spotifyUser, principalName, displayName);
                } else {
                    throw e;
                }
            }
        }
    }

    protected void handleUnapprovedUser(
            UserDto spotifyUser,
            String principalName,
            String displayName

    ) {
        userRepository.findBySpotifyPrincipalName(principalName).ifPresentOrElse(
                (userAccount) -> {
                    userRepository.save(
                            userAccount
                                    .setSpotifyEmail(spotifyUser.getEmail())
                                    .setSpotifyUsername(displayName)
                                    .setApproved(false)
                    );
                    sendUnapprovedUserNotificationEmail(userAccount);
                },
                () -> {
                    UserAccount userAccount = userRepository.save(
                            new UserAccount()
                                    .setSpotifyEmail(spotifyUser.getEmail())
                                    .setSpotifyPrincipalName(principalName)
                                    .setSpotifyUsername(displayName)
                                    .setApproved(false)
                    );
                    sendUnapprovedUserNotificationEmail(userAccount);
                }
        );
    }

    protected void sendUnapprovedUserNotificationEmail(UserAccount userAccount) {
        if (userAccount.isApprovalEmailSent()) {
            return;
        }

        final String userAccountEmail = userAccount.getSpotifyEmail();
        final String userAccountName = userAccount.getSpotifyPrincipalName();
        final String subjectAndContent = "Add user with Full Name:'" + userAccountName + "' Email:'" +
                userAccountEmail + "' to spotify Development Dashboard's Application settings User Management list";

        emailService.sendMail(
                "prtjohanson@gmail.com",
                subjectAndContent,
                subjectAndContent
        );

        userRepository.save(userAccount.setApprovalEmailSent(true));
    }

    protected void handleTopItemsSync(
            UserDto spotifyUser,
            String principalName,
            String displayName
    ) {
        final TopItemsResponseDto topArtists = spotifyApiService.getTopArtists();
        final TopItemsResponseDto topTracks = spotifyApiService.getTopTracks();

        userRepository.findBySpotifyPrincipalName(principalName).ifPresentOrElse(
                (userAccount) -> {
                    userRepository.save(
                            userAccount
                                    .setSpotifyEmail(spotifyUser.getEmail())
                                    .setSpotifyUsername(displayName)
                                    .setApproved(true)
                    );

                    userTopItemsService.clearUserTopItems(userAccount);
                    userTopItemsService.syncUserTopItemsFromDto(topArtists, userAccount);
                    userTopItemsService.syncUserTopItemsFromDto(topTracks, userAccount);
                },
                () -> {
                    UserAccount userAccount = userRepository.save(
                            new UserAccount()
                                    .setSpotifyEmail(spotifyUser.getEmail())
                                    .setSpotifyPrincipalName(principalName)
                                    .setSpotifyUsername(displayName)
                                    .setApproved(true)
                    );

                    userTopItemsService.syncUserTopItemsFromDto(topArtists, userAccount);
                    userTopItemsService.syncUserTopItemsFromDto(topTracks, userAccount);
                }
        );
    }
}

