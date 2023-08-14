package dev.coffeebeanteam.spotifyshare.dto.ui.content;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor @Accessors(chain = true)
public class TopItemsGalleryDto {
    @Getter @Setter @NoArgsConstructor @Accessors(chain = true)
    public static class Track {
        private String name;
    }
    @Getter @Setter @NoArgsConstructor @Accessors(chain = true)
    public static class Artist {
        private String spotifyId;
        private String name;
        private ArrayList<String> images = new ArrayList<>();

        private ArrayList<Track> tracks = new ArrayList<>();

        @Override
        public String toString() {
            return "\tArtist{" +
                    "\tname='" + name + '\'' +
                    "\t, images=" + images +
                    "\t, tracks=" + tracks +
                    "\t}\n";
        }
    }

    public List<List<Artist>> getArtistsAsRows(int artistsPerRow) {
        return ListUtils.partition(artists, artistsPerRow);
    }

    private ArrayList<Artist> artists = new ArrayList<>();

    @Override
    public String toString() {
        return "TopItemsGalleryDto{" +
                "artists=" + artists +
                "}\n";
    }
}
