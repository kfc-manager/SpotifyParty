package com.kalle.spotifyparty.routes.search;

import com.kalle.spotifyparty.transcripts.Track;
import com.kalle.spotifyparty.spotifyapi.ApiException;
import com.kalle.spotifyparty.spotifyapi.SpotifyAPI;
import com.kalle.spotifyparty.transcripts.ApiTrack;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchService {

    public List<Track> getSearch(String query) throws ApiException {
        List<ApiTrack> apiTracks = SpotifyAPI.searchTrack(query);
        return Track.transformTracks(apiTracks);
    }

}
