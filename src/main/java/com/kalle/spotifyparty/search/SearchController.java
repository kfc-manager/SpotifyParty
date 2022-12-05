package com.kalle.spotifyparty.search;

import com.kalle.spotifyparty.ApiException;
import com.kalle.spotifyparty.transcripts.MyError;
import com.kalle.spotifyparty.transcripts.Track;
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
    public MyError handleException(ApiException e) { //TODO optimize error status
        MyError error = new MyError();
        error.setMessage(e.getMessage());
        if (e.getStatus() == 0) {
            error.setStatus(500);
        } else {
            error.setStatus(e.getStatus());
        }
        return error;
    }

}
