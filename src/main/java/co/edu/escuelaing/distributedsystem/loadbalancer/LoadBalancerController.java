package co.edu.escuelaing.distributedsystem.loadbalancer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
public class LoadBalancerController {

    @Autowired
    private ServerRegistry registry;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/register")
    public ResponseEntity<String> registerBackend(@RequestBody Map<String, String> request) {
        String url = request.get("url");
        registry.register(url);
        return ResponseEntity.ok("Registered: " + url);
    }

    @PostMapping("/registerName")
    public ResponseEntity<String> registerName(@RequestBody Map<String, String> body) {
        String backend = registry.getNext();
        if (backend == null)
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("No backends available");

        String url = backend + "/store";
        restTemplate.postForEntity(url, body, String.class);
        return ResponseEntity.ok("Sent to: " + backend);
    }

    @GetMapping("/backends")
    public ResponseEntity<?> getBackends() {
        return ResponseEntity.ok(registry.getBackends());
    }
}