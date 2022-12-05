package com.kalle.spotifyparty.search;

import com.kalle.spotifyparty.ApiException;
import com.kalle.spotifyparty.SpotifyAPI;
import com.kalle.spotifyparty.transcripts.Track;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchService {

    public List<Track> getSearch(String query) throws ApiException {
        return SpotifyAPI.searchTrack(query);
    }

}
