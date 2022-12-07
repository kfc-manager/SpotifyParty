package com.kalle.spotifyparty.transcripts;

import java.util.List;

public class ApiTrack {

    private List<ApiArtist> artists;
    private String id;
    private ApiAlbum album;
    private String name;
    private int duration_ms;

    public ApiAlbum getAlbum() {
        return album;
    }

    public void setAlbum(ApiAlbum album) {
        this.album = album;
    }

    public List<ApiArtist> getArtists() {
        return artists;
    }

    public void setArtists(List<ApiArtist> artists) {
        this.artists = artists;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration_ms() {
        return duration_ms;
    }

    public void setDuration_ms(int duration_ms) {
        this.duration_ms = duration_ms;
    }
}
