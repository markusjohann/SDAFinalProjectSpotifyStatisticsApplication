package dev.coffeebeanteam.spotifyshare.Service;

import dev.coffeebeanteam.spotifyshare.repository.UserTopSongsRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTopSongsService {

    private UserTopSongsRepository userTopSongsRepository;

    @Autowired
    public UserTopSongsService(UserTopSongsRepository userTopSongsRepository) {
        this.userTopSongsRepository = userTopSongsRepository;
    }
}
