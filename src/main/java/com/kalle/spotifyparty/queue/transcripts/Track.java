package com.kalle.spotifyparty.queue.transcripts;

import java.util.List;

public class Track {

    private String id;
    private List<String> artists;
    private String name;
    private int duration;
    private String image;

    public Track(String id, List<String> artists, String name, int duration, String image) {
        this.id = id;
        this.artists = artists;
        this.name = name;
        this.duration = duration;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getArtists() {
        return artists;
    }

    public void setArtists(List<String> artists) {
        this.artists = artists;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
