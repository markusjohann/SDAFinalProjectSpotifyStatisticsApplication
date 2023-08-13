package dev.coffeebeanteam.spotifyshare.repository;

import dev.coffeebeanteam.spotifyshare.model.UserAccountPairing;
import dev.coffeebeanteam.spotifyshare.model.UserAccountPairingKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountPairingRepository extends JpaRepository <UserAccountPairing, UserAccountPairingKey> {
}
