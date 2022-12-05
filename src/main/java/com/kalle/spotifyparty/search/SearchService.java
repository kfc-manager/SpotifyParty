package com.kalle.spotifyparty.search;

import com.kalle.spotifyparty.spotifyapi.ApiException;
import com.kalle.spotifyparty.spotifyapi.SpotifyAPI;
import com.kalle.spotifyparty.spotifyapi.transcripts.ApiTrack;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchService {

    public List<ApiTrack> getSearch(String query) throws ApiException {
        return SpotifyAPI.searchTrack(query);
    }

}
