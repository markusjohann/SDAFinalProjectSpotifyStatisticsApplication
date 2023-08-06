package dev.coffeebeanteam.spotifyshare.listener;

import dev.coffeebeanteam.spotifyshare.dto.SpotifyUserDto;
import dev.coffeebeanteam.spotifyshare.dto.TopItemsResponseDto;
import dev.coffeebeanteam.spotifyshare.model.UserAccount;
import dev.coffeebeanteam.spotifyshare.repository.UserAccountRepository;
import dev.coffeebeanteam.spotifyshare.service.SpotifyApiService;
import dev.coffeebeanteam.spotifyshare.service.UserTopItemsService;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final UserAccountRepository userRepository;
    private final SpotifyApiService spotifyApiService;

    private final UserTopItemsService userTopItemsService;

    public AuthenticationSuccessListener(
            UserAccountRepository userRepository,
            SpotifyApiService spotifyApiService,
            UserTopItemsService userTopItemsService
    ) {
        this.userRepository = userRepository;
        this.spotifyApiService = spotifyApiService;
        this.userTopItemsService = userTopItemsService;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        if (event.getSource() instanceof OAuth2LoginAuthenticationToken) {
            final OAuth2LoginAuthenticationToken authentication = (OAuth2LoginAuthenticationToken)event.getSource();

            final String principalName = authentication.getPrincipal().getName();
            final String displayName = (String) authentication.getPrincipal().getAttributes().get("display_name");
            final String token = authentication.getAccessToken().getTokenValue();

            spotifyApiService.setToken(token);
            final SpotifyUserDto spotifyUser = spotifyApiService.getUser();

            final TopItemsResponseDto topArtists = spotifyApiService.getTopArtists();
            final TopItemsResponseDto topTracks = spotifyApiService.getTopTracks();

            userRepository.findBySpotifyPrincipalName(principalName).ifPresentOrElse(
                (userAccount) -> {
                    userRepository.save(
                            userAccount
                                    .setSpotifyEmail(spotifyUser.getEmail())
                                    .setSpotifyUsername(displayName)
                    );

                    // userTopItemsService.clearUserTopItems(userAccount);
                    userTopItemsService.syncUserTopItemsFromDto(topArtists, userAccount);
                    userTopItemsService.syncUserTopItemsFromDto(topTracks, userAccount);
                },
                () -> {
                    UserAccount userAccount = userRepository.save(
                            new UserAccount()
                                    .setSpotifyEmail(spotifyUser.getEmail())
                                    .setSpotifyPrincipalName(principalName)
                                    .setSpotifyUsername(displayName)
                    );

                    userTopItemsService.syncUserTopItemsFromDto(topArtists, userAccount);
                    userTopItemsService.syncUserTopItemsFromDto(topTracks, userAccount);
                }
            );
        }
    }
}

