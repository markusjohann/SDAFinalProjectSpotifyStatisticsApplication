package dev.coffeebeanteam.spotifyshare.repository;

import dev.coffeebeanteam.spotifyshare.Entity.UserTopArtists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserTopArtistsRepository extends JpaRepository<UserTopArtists, String> {

    @Query("SELECT uts FROM UserTopArtists uts WHERE uts.userTopArtists = ?1 AND uts.totalPlaytime = ?2 AND uts.timeRange = ?3 " +
            "AND uts.createdAt >= ?4 AND uts.updatedAt <= ?5")
    Optional<UserTopArtists> findByUserTopArtists(String userTopArtists, long totalPlaytime, String timeRange, LocalDateTime createdAt,
                                          LocalDateTime updatedAt);
}
