package dev.coffeebeanteam.spotifyshare.service;

import dev.coffeebeanteam.spotifyshare.dto.TopItemDto;
import dev.coffeebeanteam.spotifyshare.dto.TopItemsResponseDto;
import dev.coffeebeanteam.spotifyshare.model.UserAccount;
import dev.coffeebeanteam.spotifyshare.model.UserTopItem;
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

    public void clearUserTopItems(UserAccount userAccount) {
        userTopItemsRepository.deleteAllByUserAccount(userAccount);
    }

    public void syncUserTopItemsFromDto(TopItemsResponseDto topItems, UserAccount userAccount) {
        for (TopItemDto topItemDto: topItems.getItems()) {
            final UserTopItem userTopItem = new UserTopItem();

            userTopItem.setAlbum("");
            userTopItem.setName(topItemDto.getName());
            userTopItem.setType(topItemDto.getType());
            userTopItem.setUserAccount(userAccount);

            userTopItemsRepository.save(userTopItem);
        }
    }
}
