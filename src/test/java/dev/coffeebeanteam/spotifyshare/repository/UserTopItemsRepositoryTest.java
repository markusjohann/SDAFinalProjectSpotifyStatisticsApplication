package dev.coffeebeanteam.spotifyshare.repository;

import dev.coffeebeanteam.spotifyshare.model.UserAccount;
import dev.coffeebeanteam.spotifyshare.model.UserTopItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class UserTopItemsRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserTopItemsRepository userTopItemsRepository;

    private UserAccount userAccount;

    @BeforeEach
    void setUp() {
        userAccount = new UserAccount();
        userAccount.setSpotifyUsername("testUsername");
        userAccount.setSpotifyPrincipalName("testPrincipalName");
        userAccount.setSpotifyEmail("test@email.com");
        userAccount.setApproved(true);
        userAccount.setApprovalEmailSent(false);

        userAccount = entityManager.persistAndFlush(userAccount);

        UserTopItem userTopItem = new UserTopItem();
        userTopItem.setUserAccount(userAccount);

        entityManager.persistAndFlush(userTopItem);
    }

    @Test
    void findByUserAccount() {
        List<UserTopItem> foundItems = userTopItemsRepository.findByUserAccount(userAccount);
        assertThat(foundItems).isNotEmpty();
    }

    @Test
    void deleteByUserAccount() {
        userTopItemsRepository.deleteByUserAccount(userAccount);

        List<UserTopItem> foundItems = userTopItemsRepository.findByUserAccount(userAccount);
        assertThat(foundItems).isEmpty();
    }
}
