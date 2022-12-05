package com.kalle.spotifyparty.routes.queue;

import com.kalle.spotifyparty.routes.Track;
import com.kalle.spotifyparty.spotifyapi.ApiException;
import com.kalle.spotifyparty.spotifyapi.SpotifyAPI;
import com.kalle.spotifyparty.spotifyapi.transcripts.ApiArtist;
import com.kalle.spotifyparty.spotifyapi.transcripts.ApiResponse;
import com.kalle.spotifyparty.spotifyapi.transcripts.ApiTrack;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Component
public class QueueService {

    public List<Track> getQueue() throws ApiException {
        ApiResponse apiResponse = SpotifyAPI.getQueue();
        List<ApiTrack> apiTracks = Stream.concat(
                List.of(apiResponse.getCurrently_playing()).stream(),
                apiResponse.getQueue().stream()).toList();
        return Track.transformTracks(apiTracks);
    }

    public void addToQueue(String songID) throws ApiException {
        SpotifyAPI.addTrack(songID);
    }

}
