package dev.coffeebeanteam.spotifyshare.repository;

import dev.coffeebeanteam.spotifyshare.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findBySpotifyPrincipalName(String principalName);
    Page<UserAccount> findBySpotifyUsernameContainingIgnoreCase(String spotifyUsername, Pageable pageable);
}
