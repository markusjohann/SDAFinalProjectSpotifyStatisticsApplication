package dev.coffeebeanteam.spotifyshare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity @Getter @Setter @NoArgsConstructor
public class AuthorizedClientEntity {
    @Id
    private String clientRegistrationId;

    private String principalName;

    private String accessTokenType;

    private String accessTokenValue;

    private Instant accessTokenIssuedAt;

    private Instant accessTokenExpiresAt;

    private String accessTokenScopes;

    private String refreshTokenValue;

    private Instant refreshTokenIssuedAt;
}

