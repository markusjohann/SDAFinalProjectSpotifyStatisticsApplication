package dev.coffeebeanteam.spotifyshare.repository;


import dev.coffeebeanteam.spotifyshare.Entity.UserTopSongs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserTopSongsRepository extends JpaRepository<UserTopSongs, String> {

    @Query("SELECT uts FROM UserTopSongs uts WHERE uts.userTopSongs = ?1 AND uts.genre = ?2 AND uts.totalPlaytime = ?3 AND uts.timeRange = ?4 " +
            "AND uts.createdAt >= ?5 AND uts.updatedAt <= ?6")
    Optional<UserTopSongs> findByUserTopSongs(String userTopSongs, String genre, long totalPlaytime, String timeRange,
                                        LocalDateTime createdAt, LocalDateTime updatedAt);

}
