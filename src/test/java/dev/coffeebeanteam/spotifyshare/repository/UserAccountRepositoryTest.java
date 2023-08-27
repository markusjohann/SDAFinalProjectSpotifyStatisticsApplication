package dev.coffeebeanteam.spotifyshare.repository;

import dev.coffeebeanteam.spotifyshare.model.UserAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class UserAccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @BeforeEach
    public void setUp() {
        UserAccount userAccount = new UserAccount();
        userAccount.setSpotifyUsername("testUser");
        userAccount.setSpotifyPrincipalName("principalTestUser");
        userAccount.setSpotifyEmail("testuser@email.com");
        entityManager.persistAndFlush(userAccount);
    }

    @Test
    void findBySpotifyPrincipalName() {
        Optional<UserAccount> found = userAccountRepository.findBySpotifyPrincipalName("principalTestUser");

        assertThat(found).isPresent();
        assertThat(found.get().getSpotifyPrincipalName()).isEqualTo("principalTestUser");
    }

    @Test
    void findBySpotifyUsernameContainingIgnoreCase() {
        Page<UserAccount> users = userAccountRepository.findBySpotifyUsernameContainingIgnoreCase("test", PageRequest.of(0, 10));

        assertThat(users.getContent()).hasSize(1);
        assertThat(users.getContent().get(0).getSpotifyUsername()).isEqualTo("testUser");
    }
}
