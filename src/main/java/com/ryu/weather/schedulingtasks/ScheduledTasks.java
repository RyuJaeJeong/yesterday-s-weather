package com.ryu.weather.schedulingtasks;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Scheduled(fixedRate = 5000)
    public void reportHelloWorld(){
        System.out.println("Hello, world");
    }

}
