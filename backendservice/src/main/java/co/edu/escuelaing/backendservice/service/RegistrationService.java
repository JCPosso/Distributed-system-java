package co.edu.escuelaing.backendservice.service;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Component
public class RegistrationService {

    @Value("${loadbalancer.url}")
    private String loadBalancerUrl;

    @Value("${server.port}")
    private int port;

    @Value("${public.ip}")
    private String publicIp;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostConstruct
    public void register() {
        try {
            String backendUrl = "http://" + publicIp + ":" + port;
            System.out.println("Registering backend: " + backendUrl);

            restTemplate.postForEntity(
                loadBalancerUrl + "/register",
                Map.of("url", backendUrl),
                String.class
            );

            System.out.println("Successfully registered backend with Load Balancer");
        } catch (Exception e) {
            System.err.println("Failed to register backend: " + e.getMessage());
        }
    }
}