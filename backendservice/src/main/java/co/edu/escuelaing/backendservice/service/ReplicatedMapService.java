package co.edu.escuelaing.backendservice;
import org.jgroups.*;
import org.jgroups.blocks.ReplicatedHashMap;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Value;

@Service
public class ReplicatedMapService {
    private JChannel channel;
    private ReplicatedHashMap<String, String> map;

    @Value("${public.ip}")
    private String publicIp;

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

    public List<Map<String, Object>> getNames() {
        List<Map<String, Object>> namesList = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            Map<String, Object> entryMap = Map.of(
                "name", entry.getKey(),
                "timestamp", entry.getValue()
            );
            namesList.add(entryMap);
        }
        return namesList;
    }
}
