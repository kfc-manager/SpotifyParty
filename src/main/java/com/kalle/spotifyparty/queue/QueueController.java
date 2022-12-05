package com.kalle.spotifyparty.queue;

import com.kalle.spotifyparty.spotifyapi.transcripts.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public List<Queue> getQueue() {
        return service.getQueue();
    }

    @GetMapping("/add")
    public void addToQueue(@RequestParam Map<String,String> params) {
        service.addToQueue(params.get("track"));
    }

}
