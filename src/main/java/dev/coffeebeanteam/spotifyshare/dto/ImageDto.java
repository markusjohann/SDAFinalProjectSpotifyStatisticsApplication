package dev.coffeebeanteam.spotifyshare.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data @Accessors(chain = true)
public class ImageDto {
    private String url;
    private int height;
    private int width;
}
