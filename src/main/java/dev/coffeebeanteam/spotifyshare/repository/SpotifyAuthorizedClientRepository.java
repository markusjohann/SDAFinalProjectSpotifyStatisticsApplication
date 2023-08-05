package dev.coffeebeanteam.spotifyshare.repository;

import dev.coffeebeanteam.spotifyshare.model.AuthorizedClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotifyAuthorizedClientRepository extends JpaRepository<AuthorizedClientEntity, String> {
    AuthorizedClientEntity findByClientRegistrationIdAndPrincipalName(String clientRegistrationId, String principalName);
}