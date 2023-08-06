package dev.coffeebeanteam.spotifyshare.repository;

import dev.coffeebeanteam.spotifyshare.Entity.UserAccount;
import dev.coffeebeanteam.spotifyshare.Entity.UserTopItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserTopItemsRepository extends JpaRepository<UserTopItem, Long> {


    public List<UserTopItem> findByUserAccount(UserAccount userAccount);


}
