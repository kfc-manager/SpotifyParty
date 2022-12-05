package com.kalle.spotifyparty.queue;

import com.kalle.spotifyparty.spotifyapi.SpotifyAPI;
import com.kalle.spotifyparty.spotifyapi.transcripts.Queue;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QueueService {

    public List<Queue> getQueue() {
        return List.of(SpotifyAPI.getQueue());
    }

    public void addToQueue(String songID) {
        SpotifyAPI.addTrack(songID);
    }

}
