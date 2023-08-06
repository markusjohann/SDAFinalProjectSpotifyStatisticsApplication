package dev.coffeebeanteam.spotifyshare.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserTopItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "user_account")
    private UserAccount userAccount;

    //The name of the track
    private String name;
    private String artists;

    //the number of the track. If an album has several discs, the track number is the number on the specified disc
    private Integer track_number;

    //total playtime or the total number of times the user has listened to the top songs
    private long totalPlaytime;

    //The time range for which the top songs are calculated "short_term," "medium_term," "long_term" etc
    private String timeRange;
    private String album;

    //images of the artist in various sizes, the widest first.
    private String image;

    private String type;

    //The offset of the items returned (as set in the query or by default)
    private Integer offset;

    //the total number of items available to return
    private Integer total;

    //ability to show when were the stats first created and updated if wanted
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


}
