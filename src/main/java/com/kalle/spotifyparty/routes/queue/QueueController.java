package com.kalle.spotifyparty.routes.queue;

import com.kalle.spotifyparty.transcripts.ErrorBody;
import com.kalle.spotifyparty.transcripts.Track;
import com.kalle.spotifyparty.spotifyapi.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/queue")
public class QueueController {

    private QueueService service;

    @Autowired
    public QueueController(QueueService service) {
        this.service = service;
    }

    @GetMapping("/get")
    public List<Track> getQueue() throws ApiException {
        return service.getQueue();
    }

    @GetMapping("/add")
    public void addToQueue(@RequestParam Map<String,String> params) throws ApiException {
        service.addToQueue(params.get("track"));
    }

    @ExceptionHandler(ApiException.class)
    public ErrorBody handleException(ApiException e) { //TODO optimize error status
        return e.getErrorBody();
    }

}
