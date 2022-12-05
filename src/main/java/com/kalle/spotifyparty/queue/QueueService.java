package com.kalle.spotifyparty.queue;

import com.kalle.spotifyparty.ApiException;
import com.kalle.spotifyparty.SpotifyAPI;
import com.kalle.spotifyparty.transcripts.ApiResponse;
import org.springframework.stereotype.Component;

@Component
public class QueueService {

    public ApiResponse getQueue() throws ApiException {
        return SpotifyAPI.getQueue();
    }

    public void addToQueue(String songID) throws ApiException {
        SpotifyAPI.addTrack(songID);
    }

}
