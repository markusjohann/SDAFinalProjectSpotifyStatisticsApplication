package dev.coffeebeanteam.spotifyshare.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable @Getter @Setter @NoArgsConstructor
public class TrackArtistPairingKey implements Serializable {
    @Column(name="artists")
    private long artists;

    @Column(name="track_number")
    private long track_number;
}
