package com.kalle.spotifyparty.search;

import com.kalle.spotifyparty.spotifyapi.SpotifyAPI;
import com.kalle.spotifyparty.spotifyapi.transcripts.Track;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/search")
public class SearchController {

    private SearchService service;

    public SearchController(SearchService service) {
        this.service = service;
    }

    @GetMapping
    public List<Track> getSearch(@RequestParam Map<String,String> params) {
        return SpotifyAPI.searchTrack(params.get("q"));
    }

}
