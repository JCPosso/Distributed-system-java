package co.edu.escuelaing.backendservice;
import org.jgroups.*;
import org.jgroups.blocks.ReplicatedHashMap;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Map;

@Service
public class ReplicatedMapService {
    private JChannel channel;
    private ReplicatedHashMap<String, String> map;

    @PostConstruct
    public void init() throws Exception {
        channel = new JChannel();
        map = new ReplicatedHashMap<>(channel);
        channel.connect("DistributedCluster");
        map.start(10000);
        System.out.println("Joined cluster with current data: " + map);
    }

    public void put(String key, String value) {
        map.put(key, value);
        System.out.println("Added: " + key + " = " + value);
    }

    public Map<String, String> getAll() {
        return map;
    }
}
