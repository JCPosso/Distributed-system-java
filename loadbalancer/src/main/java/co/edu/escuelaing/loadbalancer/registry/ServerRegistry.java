package co.edu.escuelaing.loadbalancer.registry;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ServerRegistry {

    private final List<String> backends = new CopyOnWriteArrayList<>();
    private final AtomicInteger index = new AtomicInteger(0);

    public void register(String backendUrl) {
        if (!backends.contains(backendUrl)) {
            backends.add(backendUrl);
            System.out.println("Registered backend: " + backendUrl);
        }
    }

    public String getNext() {
        if (backends.isEmpty()) return null;
        return backends.get(index.getAndUpdate(i -> (i + 1) % backends.size()));
    }

    public List<String> getBackends() {
        return backends;
    }
}
