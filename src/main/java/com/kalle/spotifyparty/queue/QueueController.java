package com.kalle.spotifyparty.queue;

import com.kalle.spotifyparty.ApiException;
import com.kalle.spotifyparty.transcripts.ApiResponse;
import com.kalle.spotifyparty.transcripts.MyError;
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
    public ApiResponse getQueue() throws ApiException {
        return service.getQueue();
    }

    @GetMapping("/add")
    public void addToQueue(@RequestParam Map<String,String> params) throws ApiException {
        service.addToQueue(params.get("track"));
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
