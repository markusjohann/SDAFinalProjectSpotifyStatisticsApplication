package dev.coffeebeanteam.spotifyshare.service;

import dev.coffeebeanteam.spotifyshare.model.AuthorizedClientEntity;
import dev.coffeebeanteam.spotifyshare.repository.SpotifyAuthorizedClientRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.stereotype.Service;

@Service
public class JpaOAuth2AuthorizedClientService implements OAuth2AuthorizedClientService {
    private SpotifyAuthorizedClientRepository authorizedClientRepository;

    private ClientRegistrationRepository clientRegistrationRepository;

    public JpaOAuth2AuthorizedClientService(
            SpotifyAuthorizedClientRepository authorizedClientRepository,
            ClientRegistrationRepository clientRegistrationRepository
    ) {
        this.authorizedClientRepository = authorizedClientRepository;
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Override
    public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(
            String clientRegistrationId,
            String principalName
    ) {
        final AuthorizedClientEntity authorizedClientEntity = authorizedClientRepository
                .findByClientRegistrationIdAndPrincipalName(clientRegistrationId, principalName);
        if (authorizedClientEntity == null) {
            return null;
        }

        final OAuth2AccessToken oAuth2AccessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                authorizedClientEntity.getAccessTokenValue(),
                authorizedClientEntity.getAccessTokenIssuedAt(),
                authorizedClientEntity.getAccessTokenExpiresAt());

        final OAuth2RefreshToken oAuth2RefreshToken = new OAuth2RefreshToken(
                authorizedClientEntity.getRefreshTokenValue(),
                authorizedClientEntity.getRefreshTokenIssuedAt());
        final ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(clientRegistrationId);

        return (T) new OAuth2AuthorizedClient(clientRegistration, principalName, oAuth2AccessToken, oAuth2RefreshToken);
    }

    @Override
    public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
        final AuthorizedClientEntity authorizedClientEntity = new AuthorizedClientEntity();
        authorizedClientEntity.setClientRegistrationId(authorizedClient.getClientRegistration().getRegistrationId());
        authorizedClientEntity.setPrincipalName(principal.getName());
        authorizedClientEntity.setAccessTokenType(authorizedClient.getAccessToken().getTokenType().getValue());
        authorizedClientEntity.setAccessTokenValue(authorizedClient.getAccessToken().getTokenValue());
        authorizedClientEntity.setAccessTokenIssuedAt(authorizedClient.getAccessToken().getIssuedAt());
        authorizedClientEntity.setAccessTokenExpiresAt(authorizedClient.getAccessToken().getExpiresAt());
        authorizedClientEntity.setAccessTokenScopes(String.join(",", authorizedClient.getAccessToken().getScopes()));

        if (authorizedClient.getRefreshToken() != null) {
            authorizedClientEntity.setRefreshTokenValue(authorizedClient.getRefreshToken().getTokenValue());
            authorizedClientEntity.setRefreshTokenIssuedAt(authorizedClient.getRefreshToken().getIssuedAt());
        }

        authorizedClientRepository.save(authorizedClientEntity);
    }

    @Override
    public void removeAuthorizedClient(String clientRegistrationId, String principalName) {
        final AuthorizedClientEntity authorizedClientEntity = authorizedClientRepository
                .findByClientRegistrationIdAndPrincipalName(clientRegistrationId, principalName);

        if (authorizedClientEntity != null) {
            authorizedClientRepository.delete(authorizedClientEntity);
        }
    }
}
