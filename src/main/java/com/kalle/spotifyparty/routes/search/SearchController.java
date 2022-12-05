package com.kalle.spotifyparty.routes.search;

import com.kalle.spotifyparty.routes.Track;
import com.kalle.spotifyparty.spotifyapi.ApiException;
import com.kalle.spotifyparty.spotifyapi.transcripts.ApiError;
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
    public ApiError handleException(ApiException e) { //TODO optimize error status
        ApiError error = new ApiError();
        error.setMessage(e.getMessage());
        if (e.getStatus() == 0) {
            error.setStatus(500);
        } else {
            error.setStatus(e.getStatus());
        }
        return error;
    }

}
