package com.kalle.spotifyparty.spotifyapi.transcripts;

import java.util.List;

public class Queue {

    private Track currently_playing;
    private List<Track> queue;

    public Track getCurrently_playing() {
        return currently_playing;
    }

    public void setCurrently_playing(Track currently_playing) {
        this.currently_playing = currently_playing;
    }

    public List<Track> getQueue() {
        return queue;
    }

    public void setQueue(List<Track> queue) {
        this.queue = queue;
    }

}
