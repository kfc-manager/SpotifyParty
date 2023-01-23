package com.kalle.spotifyparty.spotifyapi;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.kalle.spotifyparty.transcripts.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Stream;

@RestController
public class SpotifyAPI { //TODO automatically refresh TOKEN

    private static final String CLIENT_ID = "";
    private static final String CLIENT_SECRET = "";

    private static String TOKEN;
    private static String refresh_token;

    private static final String scope = "user-read-private user-read-currently-playing user-read-playback-state user-modify-playback-state";
    private static final String redirectURI = "http://localhost:8080/callback";
    private static String state;

    private static String generateRandomString(int length) {
        String characters = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890";
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            builder.append(characters.charAt(index));
        }
        return builder.toString();
    }

    private static String getEncodedDetails() {
        String originalInput = CLIENT_ID + ":" + CLIENT_SECRET;
        String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());
        return encodedString;
    }

    private static List<Track> transformTracks(List<ApiTrack> apiTracks) { //function to transform the transcripts (to cut out unrelevant information)
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

    private static ApiResponse sendRequest(HttpRequest request) throws ApiException {
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();
            ApiResponse apiResponse = gson.fromJson(httpResponse.body(), ApiResponse.class);
            if (apiResponse.getError() != null) {
                ApiError error = apiResponse.getError();
                throw new ApiException(error.getStatus(), error.getMessage());
            }
            return apiResponse;
        } catch (IOException | InterruptedException | JsonSyntaxException e) {
            throw new ApiException(e.getMessage());
        } catch (NullPointerException e) {
            return null; //apiResponse null for addTrack(), since no response body is received TODO find better solution
        }
    }

    @GetMapping("/login")
    public RedirectView login(RedirectAttributes attributes) {
        state = generateRandomString(16);
        attributes.addFlashAttribute("flashAttribute", "redirectWithRedirectView");
        attributes.addAttribute("attribute", "redirectWithRedirectView");
        UriComponents uri = UriComponentsBuilder.newInstance()
                .scheme("https").host("accounts.spotify.com").path("/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", CLIENT_ID)
                .queryParam("scope", scope)
                .queryParam("redirect_uri", redirectURI)
                .queryParam("state", state)
                .build().encode();
        return new RedirectView(uri.toString());
    }

    @GetMapping(path = "/callback")
    public void callback(@RequestParam() Map<String, String> params) throws ApiException {
        if (!state.equals(params.get("state"))) throw new ApiException(502, "State does not match.");
        HttpRequest request = HttpRequest.newBuilder(URI.create("https://accounts.spotify.com/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString("code=" + params.get("code")
                        + "&redirect_uri=" + redirectURI
                        + "&grant_type=authorization_code"))
                .header("Authorization", "Basic " + getEncodedDetails())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();
        ApiResponse apiResponse = sendRequest(request);
        TOKEN = apiResponse.getAccess_token();
        refresh_token = apiResponse.getRefresh_token();
    }

    @ExceptionHandler(ApiException.class)
    public ErrorBody handleException(ApiException e) { //TODO optimize error status
        return e.getErrorBody();
    }

    public static void updateToken() throws ApiException { //TODO find better method to refresh token (no new refresh token gets send)
        HttpRequest request = HttpRequest.newBuilder(URI.create("https://accounts.spotify.com/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString("grant_type=refresh_token"
                        + "&refresh_token=" + refresh_token))
                .header("Authorization", "Basic " + getEncodedDetails())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();
        ApiResponse apiResponse = sendRequest(request);
        TOKEN = apiResponse.getAccess_token();
    }

    public static List<Track> getQueue() throws ApiException {
        HttpRequest request = HttpRequest.newBuilder(URI.create("https://api.spotify.com/v1/me/player/queue"))
                .header("Authorization", "Bearer " + TOKEN)
                .build();
        ApiResponse apiResponse = sendRequest(request);
        if (apiResponse.getCurrently_playing() == null) {
            throw new ApiException(503, "Player is currently not active.");
        }
        List<ApiTrack> apiTracks = Stream.concat(
                List.of(apiResponse.getCurrently_playing()).stream(),
                apiResponse.getQueue().stream()).toList();
        List<Track> tracks = transformTracks(apiTracks); //transform retrieved tracks to our format
        return tracks;
    }

    public static void addTrack(String songID) throws ApiException {
        UriComponents uri = UriComponentsBuilder.newInstance()
                .scheme("https").host("api.spotify.com").path("/v1/me/player/queue")
                .queryParam("uri", "spotify:track:" + songID)
                .build().encode();
        HttpRequest request = HttpRequest.newBuilder(URI.create(uri.toString()))
                .POST(HttpRequest.BodyPublishers.ofString("")) //TODO POST request without body?
                .header("Authorization", "Bearer " + TOKEN)
                .build();
        sendRequest(request);
    }

    public static List<Track> searchTrack(String query) throws ApiException {
        UriComponents uri = UriComponentsBuilder.newInstance()
                .scheme("https").host("api.spotify.com").path("/v1/search")
                .queryParam("q", query)
                .queryParam("type", "track")
                .build().encode();
        HttpRequest request = HttpRequest.newBuilder(URI.create(uri.toString()))
                .header("Authorization", "Bearer " + TOKEN)
                .build();
        ApiResponse apiResponse = sendRequest(request);
        List<Track> tracks = transformTracks(apiResponse.getTracks().getItems()); //transform retrieved tracks to our format
        return tracks;
    }

}
