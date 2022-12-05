package com.kalle.spotifyparty.transcripts;

import java.util.List;

public class ApiResponse {

    private MyError error;

    private String access_token;
    private String token_type;
    private String scope;
    private int expires_in;
    private String refresh_token;

    private Track currently_playing;
    private List<Track> queue;

    private TrackList tracks;

    public MyError getError() {
        return error;
    }

    public void setError(MyError error) {
        this.error = error;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

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

    public TrackList getTracks() {
        return tracks;
    }

    public void setTracks(TrackList tracks) {
        this.tracks = tracks;
    }
}
