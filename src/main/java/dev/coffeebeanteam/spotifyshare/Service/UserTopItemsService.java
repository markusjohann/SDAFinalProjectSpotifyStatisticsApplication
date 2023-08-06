package dev.coffeebeanteam.spotifyshare.Service;

import dev.coffeebeanteam.spotifyshare.repository.UserTopItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//this class is for saving data into database


@Service
public class UserTopItemsService {

    private UserTopItemsRepository userTopItemsRepository;

    @Autowired
    public UserTopItemsService(UserTopItemsRepository userTopTrackRepository) {
        this.userTopItemsRepository = userTopTrackRepository;
    }





}
