package dev.coffeebeanteam.spotifyshare.repository;

import dev.coffeebeanteam.spotifyshare.model.TrackArtistPairing;
import dev.coffeebeanteam.spotifyshare.model.TrackArtistPairingKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackArtistPairingRepository extends JpaRepository <TrackArtistPairing, TrackArtistPairingKey> {



}
