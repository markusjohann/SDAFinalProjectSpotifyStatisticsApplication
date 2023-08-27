package dev.coffeebeanteam.spotifyshare.dto.ui;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain = true)
public class UserAccountDto {
    public long userId;
    public String displayName;
}
