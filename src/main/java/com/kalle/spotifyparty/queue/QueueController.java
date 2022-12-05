package com.kalle.spotifyparty.queue;

import com.kalle.spotifyparty.queue.transcripts.Track;
import com.kalle.spotifyparty.spotifyapi.ApiException;
import com.kalle.spotifyparty.spotifyapi.transcripts.ApiError;
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
