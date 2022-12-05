package com.kalle.spotifyparty.routes;

import com.kalle.spotifyparty.spotifyapi.transcripts.ApiArtist;
import com.kalle.spotifyparty.spotifyapi.transcripts.ApiTrack;

import java.util.ArrayList;
import java.util.List;

public class Track {

    private String id;
    private List<String> artists;
    private String name;
    private int duration;
    private String image;

    public static List<Track> transformTracks(List<ApiTrack> apiTracks) {
        List<Track> tracks = new ArrayList<>();
        for (ApiTrack apiTrack : apiTracks) {
            List<String> artists = new ArrayList<>();
            for (ApiArtist apiArtist : apiTrack.getArtists()) {
                artists.add(apiArtist.getName());
            }
            Track track = new Track(
                    apiTrack.getId(),
                    artists,
                    apiTrack.getName(),
                    apiTrack.getDuration_ms(),
                    apiTrack.getAlbum().getImages().get(0).getUrl()
            );
            tracks.add(track);
        }
        return tracks;
    }

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
