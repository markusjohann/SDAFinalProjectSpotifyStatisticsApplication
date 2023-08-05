package dev.coffeebeanteam.spotifyshare.configuration;

import dev.coffeebeanteam.spotifyshare.service.JpaOAuth2AuthorizedClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
class SecurityConfiguration {
    final private SpotifyApiConfiguration spotifyApiConfiguration;

    final private JpaOAuth2AuthorizedClientService authorizedClientService;

    SecurityConfiguration(
            SpotifyApiConfiguration spotifyApiConfiguration,
            @Lazy JpaOAuth2AuthorizedClientService authorizedClientService
            ) {
        this.spotifyApiConfiguration = spotifyApiConfiguration;
        this.authorizedClientService = authorizedClientService;
    }

    @Bean
    @Autowired
    SecurityFilterChain getSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(csrf -> csrf.disable());

        httpSecurity.authorizeHttpRequests(
                auth -> auth.anyRequest().authenticated()
        );

        httpSecurity.oauth2Login(oa2login -> oa2login
                .authorizedClientService(authorizedClientService)
                .defaultSuccessUrl("/", true));

        return httpSecurity.build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration.Builder builder = ClientRegistration.withRegistrationId("spotify")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/{action}/oauth2/code/{registrationId}")
                .scope("user-read-private,user-read-email,user-top-read")
                .authorizationUri("https://accounts.spotify.com/authorize")
                .tokenUri("https://accounts.spotify.com/api/token")
                .userInfoUri("https://api.spotify.com/v1/me")
                .userNameAttributeName("id")
                .clientName("spotify")
                .clientId(spotifyApiConfiguration.getApiClientId())
                .clientSecret(spotifyApiConfiguration.getApiClientSecret());

        return new InMemoryClientRegistrationRepository(
                builder.build());
    }
}
