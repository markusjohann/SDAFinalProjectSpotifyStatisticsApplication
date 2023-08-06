package dev.coffeebeanteam.spotifyshare.repository;

import dev.coffeebeanteam.spotifyshare.model.UserAccount;
import dev.coffeebeanteam.spotifyshare.model.UserTopItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserTopItemsRepository extends JpaRepository<UserTopItem, Long> {


    public List<UserTopItem> findByUserAccount(UserAccount userAccount);

    void deleteByUserAccount(UserAccount userAccount);

}
