package dev.coffeebeanteam.spotifyshare.repository;

import dev.coffeebeanteam.spotifyshare.model.SharingStatus;
import dev.coffeebeanteam.spotifyshare.model.UserAccount;
import dev.coffeebeanteam.spotifyshare.model.UserAccountSharing;
import dev.coffeebeanteam.spotifyshare.model.UserAccountSharingKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAccountSharingRepository extends JpaRepository <UserAccountSharing, UserAccountSharingKey> {
    List<UserAccountSharing> findByRequesterAndStatus(UserAccount requester, SharingStatus status);

    List<UserAccountSharing> findByRequestReceiverAndStatus(UserAccount requestReceiver, SharingStatus status);
}
