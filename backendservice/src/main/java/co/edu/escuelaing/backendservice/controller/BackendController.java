package co.edu.escuelaing.backendservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;
import co.edu.escuelaing.backendservice.ReplicatedMapService;

@RestController
public class BackendController {

    @Autowired
    private ReplicatedMapService replicatedMapService;

    @PostMapping("/store")
    public String store(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        String timestamp = Instant.now().toString();
        replicatedMapService.put(name, timestamp);
        return "Stored " + name + " at " + timestamp;
    }

    @GetMapping("/state")
    public Map<String, String> state() {
        return replicatedMapService.getAll();
    }
}
