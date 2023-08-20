package dev.coffeebeanteam.spotifyshare.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TrackArtistPairing {
    @EmbeddedId
    private UserAccountPairingKey id;


    @ManyToOne
    @MapsId("artists")
    @JoinColumn(name="artists")
    private UserTopItem artists;

    @ManyToOne
    @MapsId("track_number")
    @JoinColumn(name="track_number")
    private UserTopItem track_number;

}
