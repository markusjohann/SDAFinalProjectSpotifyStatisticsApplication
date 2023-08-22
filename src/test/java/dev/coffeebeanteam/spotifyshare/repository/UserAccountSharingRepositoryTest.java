package dev.coffeebeanteam.spotifyshare.repository;

import dev.coffeebeanteam.spotifyshare.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class UserAccountSharingRepositoryTest {

    @Autowired
    private UserAccountSharingRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindByRequesterAndStatus() {
        UserAccount requester = new UserAccount()
                .setSpotifyUsername("requester")
                .setSpotifyPrincipalName("principalRequester")
                .setSpotifyEmail("requester@email.com");

        UserAccount receiver = new UserAccount()
                .setSpotifyUsername("receiver")
                .setSpotifyPrincipalName("principalReceiver")
                .setSpotifyEmail("receiver@email.com");

        entityManager.persist(requester);
        entityManager.persist(receiver);

        UserAccountSharing sharing = new UserAccountSharing()
                .setId(new UserAccountSharingKey()
                        .setUserAccountIdReceiver(receiver.getId())
                        .setUserAccountIdRequester(requester.getId()))                .setRequester(requester)
                .setRequestReceiver(receiver)
                .setStatus(SharingStatus.PENDING);

        entityManager.persist(sharing);

        List<UserAccountSharing> results = repository.findByRequesterAndStatus(requester, SharingStatus.PENDING);

        assertThat(results).isNotEmpty();
        assertThat(results.get(0).getRequestReceiver()).isEqualTo(receiver);
    }

    @Test
    void testFindByRequestReceiverAndStatus() {
        UserAccount requester = new UserAccount()
                .setSpotifyUsername("requester2")
                .setSpotifyPrincipalName("principalRequester2")
                .setSpotifyEmail("requester2@email.com");

        UserAccount receiver = new UserAccount()
                .setSpotifyUsername("receiver2")
                .setSpotifyPrincipalName("principalReceiver2")
                .setSpotifyEmail("receiver2@email.com");

        entityManager.persist(requester);
        entityManager.persist(receiver);

        UserAccountSharing sharing = new UserAccountSharing()
                .setId(new UserAccountSharingKey()
                        .setUserAccountIdReceiver(receiver.getId())
                        .setUserAccountIdRequester(requester.getId()))
                .setRequester(requester)
                .setRequestReceiver(receiver)
                .setStatus(SharingStatus.PENDING);

        entityManager.persist(sharing);

        List<UserAccountSharing> results = repository.findByRequestReceiverAndStatus(receiver, SharingStatus.PENDING);

        assertThat(results).isNotEmpty();
        assertThat(results.get(0).getRequester()).isEqualTo(requester);
    }
}
