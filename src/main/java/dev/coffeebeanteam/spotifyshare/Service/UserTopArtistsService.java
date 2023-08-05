package dev.coffeebeanteam.spotifyshare.Service;

import dev.coffeebeanteam.spotifyshare.repository.UserTopArtistsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTopArtistsService {

    private UserTopArtistsRepository userTopArtistsRepository;



    @Autowired
    public UserTopArtistsService (UserTopArtistsRepository getUserTopArtistsRepository) {
        this.userTopArtistsRepository = userTopArtistsRepository;
    }

}
