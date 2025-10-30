package co.edu.escuelaing.loadbalancer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import co.edu.escuelaing.loadbalancer.registry.ServerRegistry;
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

    @GetMapping("/names")
    public List<Map<String, Object>> getAllNames() {
        List<String> backends = registry.getBackends();
        List<Map<String, Object>> allNames = new ArrayList<>();
        for (String backend : backends) {
            try {
                String url = backend + "/names";
                ResponseEntity<List<Map<String, Object>>> responseEntity =
                        restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Map<String, Object>>>() {});
                List<Map<String, Object>> response = responseEntity.getBody();
                if (response != null) {
                    for (Map<String, Object> item : response) {
                        item.put("backend", backend);
                        allNames.add(item);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error fetching from " + backend + ": " + e.getMessage());
            }
        }
        return allNames;
    }
}
