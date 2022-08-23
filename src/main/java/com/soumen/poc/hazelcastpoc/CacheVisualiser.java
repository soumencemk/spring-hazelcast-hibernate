package com.soumen.poc.hazelcastpoc;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.hibernate.serialization.Value;
import com.hazelcast.map.IMap;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.stream.Stream;

@Component
public class CacheVisualiser {

    @Scheduled(fixedDelay = 10000)
    public void cachePeek() {

        IMap<Object, Value> fooCache =
                Stream.concat(Hazelcast.getAllHazelcastInstances().stream(), HazelcastClient.getAllHazelcastClients().stream())
                        .findAny().orElseThrow(IllegalStateException::new)
                        .getMap(Employee.class.getName());

        System.out.println(LocalTime.now());
        System.out.println("size: " + fooCache.size());
        fooCache.forEach((k, v) -> System.out.println("    " + k.toString() + ":" + v.getValue()));
        System.out.println();
    }
}
