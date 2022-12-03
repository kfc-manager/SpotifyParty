package com.kalle.spotifyparty.spotifyapi;

import com.google.gson.Gson;
import com.kalle.spotifyparty.spotifyapi.transcripts.AuthenticationDetails;
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

@RestController
public class Authentication {

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
    public void callback(@RequestParam() Map<String, String> params) {
        if (!state.equals(params.get("state"))) return; //TODO throw an error
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create("https://accounts.spotify.com/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString("code=" + params.get("code")
                        + "&redirect_uri=" + redirectURI
                        + "&grant_type=authorization_code"))
                .header("Authorization", "Basic " + getEncodedDetails())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();
            AuthenticationDetails authenticationDetails = gson.fromJson(response.body(), AuthenticationDetails.class);
            TOKEN = authenticationDetails.getAccess_token();
            refresh_token = authenticationDetails.getRefresh_token();
        } catch (IOException e) {
            //TODO
        } catch (InterruptedException e) {
            //TODO
        }
    }

    public static void updateToken() { //TODO find better method to refresh token (no new refresh token gets send)
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create("https://accounts.spotify.com/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString("grant_type=refresh_token"
                        + "&refresh_token=" + refresh_token))
                .header("Authorization", "Basic " + getEncodedDetails())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();
            AuthenticationDetails authenticationDetails = gson.fromJson(response.body(), AuthenticationDetails.class);
            TOKEN = authenticationDetails.getAccess_token();
        } catch (IOException e) {
            //TODO
        } catch (InterruptedException e) {
            //TODO
        }
    }

    public static String getToken() {
        return TOKEN;
    }

}
