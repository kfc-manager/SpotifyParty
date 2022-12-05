package com.kalle.spotifyparty.search;

import com.kalle.spotifyparty.spotifyapi.SpotifyAPI;
import com.kalle.spotifyparty.spotifyapi.transcripts.Track;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchService {

    public List<Track> getSearch(String query) {
        return SpotifyAPI.searchTrack(query);
    }

}
