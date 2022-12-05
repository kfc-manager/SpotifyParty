package com.kalle.spotifyparty;

import com.google.gson.Gson;
import com.kalle.spotifyparty.transcripts.ApiResponse;
import com.kalle.spotifyparty.transcripts.MyError;
import com.kalle.spotifyparty.transcripts.Track;
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
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
public class SpotifyAPI {

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

    private static ApiResponse sendRequest(HttpRequest request) throws ApiException {
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();
            ApiResponse apiResponse = gson.fromJson(httpResponse.body(), ApiResponse.class);
            if (apiResponse.getError() != null) {
                MyError error = apiResponse.getError();
                throw new ApiException(error.getStatus(), error.getMessage());
            }
            return apiResponse;
        } catch (IOException | InterruptedException e) {
            throw new ApiException(e.getMessage());
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
        if (!state.equals(params.get("state"))) return; //TODO throw an error
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

    public static ApiResponse getQueue() throws ApiException {
        HttpRequest request = HttpRequest.newBuilder(URI.create("https://api.spotify.com/v1/me/player/queue"))
                .header("Authorization", "Bearer " + TOKEN)
                .build();
        ApiResponse apiResponse = sendRequest(request);
        return apiResponse;
    }

    public static void addTrack(String songID) throws ApiException {
        UriComponents uri = UriComponentsBuilder.newInstance()
                .scheme("https").host("api.spotify.com").path("/v1/me/player/queue")
                .queryParam("uri", "spotify:track:" + songID)
                .build().encode();
        HttpRequest request = HttpRequest.newBuilder(URI.create(uri.toString()))
                .POST(HttpRequest.BodyPublishers.ofString("")) //TODO POST request without body
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
        return apiResponse.getTracks().getItems();
    }

}
