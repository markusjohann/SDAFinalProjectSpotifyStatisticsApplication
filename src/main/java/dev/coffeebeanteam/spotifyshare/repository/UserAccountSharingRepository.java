package dev.coffeebeanteam.spotifyshare.repository;

import dev.coffeebeanteam.spotifyshare.model.UserAccountSharing;
import dev.coffeebeanteam.spotifyshare.model.UserAccountSharingKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountPairingRepository extends JpaRepository <UserAccountSharing, UserAccountSharingKey> {
}
