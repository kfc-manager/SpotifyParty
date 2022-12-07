package com.kalle.spotifyparty.routes.search;

import com.kalle.spotifyparty.transcripts.ErrorBody;
import com.kalle.spotifyparty.transcripts.Track;
import com.kalle.spotifyparty.spotifyapi.ApiException;
import org.springframework.web.bind.annotation.*;

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
    public List<Track> getSearch(@RequestParam Map<String,String> params) throws ApiException {
        return service.getSearch(params.get("q"));
    }

    @ExceptionHandler(ApiException.class)
    public ErrorBody handleException(ApiException e) { //TODO optimize error status
        return e.getErrorBody();
    }

}
