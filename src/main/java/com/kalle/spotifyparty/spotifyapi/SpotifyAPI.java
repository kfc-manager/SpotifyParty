package com.kalle.spotifyparty.spotifyapi;

import com.google.gson.Gson;
import com.kalle.spotifyparty.spotifyapi.transcripts.Queue;
import com.kalle.spotifyparty.spotifyapi.transcripts.Track;
import com.kalle.spotifyparty.spotifyapi.transcripts.TrackList;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class SpotifyAPI {

    //TODO requests to Spotify Web API endpoints

    public static Queue getQueue() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create("https://api.spotify.com/v1/me/player/queue"))
                .header("Authorization", "Bearer " + Authentication.getToken()).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();
            Queue queue = gson.fromJson(response.body(), Queue.class);
            return queue;
        } catch (IOException e) {
            //TODO
        } catch (InterruptedException e) {
            //TODO
        }
        return null;
    }

    public static void addTrack(String songID) {
        UriComponents uri = UriComponentsBuilder.newInstance()
                .scheme("https").host("api.spotify.com").path("/v1/me/player/queue")
                .queryParam("uri", "spotify:track:" + songID)
                .build().encode();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(uri.toString()))
                .POST(HttpRequest.BodyPublishers.ofString("")) //TODO POST request without body
                .header("Authorization", "Bearer " + Authentication.getToken())
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            //TODO
        } catch (InterruptedException e) {
            //TODO
        }
    }

    public static List<Track> searchTrack(String query) {
        UriComponents uri = UriComponentsBuilder.newInstance()
                .scheme("https").host("api.spotify.com").path("/v1/search")
                .queryParam("q", query)
                .queryParam("type", "track")
                .build().encode();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(uri.toString()))
                .header("Authorization", "Bearer " + Authentication.getToken())
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();
            TrackList searchResult = gson.fromJson(response.body(),TrackList.class);
            return searchResult.getTracks().getItems();
        } catch (IOException e) {
            //TODO
        } catch (InterruptedException e) {
            //TODO
        }
        return null;
    }


}
