package com.kalle.spotifyparty.queue;

import com.kalle.spotifyparty.queue.transcripts.Track;
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

    public void addToQueue(String songID) throws ApiException {
        SpotifyAPI.addTrack(songID);
    }

}
