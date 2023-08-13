package dev.coffeebeanteam.spotifyshare.service;

import dev.coffeebeanteam.spotifyshare.dto.TopItemDto;
import dev.coffeebeanteam.spotifyshare.dto.TopItemsResponseDto;
import dev.coffeebeanteam.spotifyshare.model.UserAccount;
import dev.coffeebeanteam.spotifyshare.model.UserTopItem;
import dev.coffeebeanteam.spotifyshare.repository.UserTopItemsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserTopItemsService {

    private UserTopItemsRepository userTopItemsRepository;

    @Autowired
    public UserTopItemsService(UserTopItemsRepository userTopTrackRepository) {
        this.userTopItemsRepository = userTopTrackRepository;
    }

    @Transactional
    public void clearUserTopItems(UserAccount userAccount) {
        userTopItemsRepository.deleteByUserAccount(userAccount);
    }

    @Transactional
    public void syncUserTopItemsFromDto(TopItemsResponseDto topItems, UserAccount userAccount) {
        for (TopItemDto topItemDto: topItems.getItems()) {
            userTopItemsRepository.save(
                new UserTopItem()
                        .setName(topItemDto.getName())
                        .setType(topItemDto.getType())
                        .setImages(
                                topItemDto.getImages() != null ?
                                    topItemDto.getImages().stream().map(
                                            (image) -> image.getUrl()
                                    ).toList() :
                                        new ArrayList<>()
                        )
                        .setUserAccount(userAccount)
            );
        }
    }

    public List<UserTopItem> getTopItemsByUser(UserAccount userAccount) {
        return this.userTopItemsRepository.findByUserAccount(userAccount);
    }
}
