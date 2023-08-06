package dev.coffeebeanteam.spotifyshare.Controller;

import dev.coffeebeanteam.spotifyshare.Entity.UserAccount;
import dev.coffeebeanteam.spotifyshare.repository.UserAccountRepository;
import dev.coffeebeanteam.spotifyshare.repository.UserTopItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


// this class is for displaying data to the user


@Controller
public class UserTopItemsController {

    private UserTopItemsRepository userTopItemsRepository;

    private UserAccountRepository userAccountRepository;


    @Autowired
    public UserTopItemsController(UserTopItemsRepository userTopItemsRepository, UserAccountRepository userAccountRepository) {
        this.userTopItemsRepository = userTopItemsRepository;
        this.userAccountRepository = userAccountRepository;
    }


    // this method saves usernames to database
    @GetMapping("/user/")
    @ResponseBody
    public String topN() {

        UserAccount userAccount = new UserAccount();
        userAccount.setSpotifyUsername("jaanus");
        userAccountRepository.save(userAccount);
        //userTopItemsRepository.findByUserAccount();

        return "";
    }

}
