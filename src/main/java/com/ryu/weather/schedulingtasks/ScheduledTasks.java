package com.ryu.weather.schedulingtasks;

import com.ryu.weather.dto.ForecastDTO;
import com.ryu.weather.dto.LocationDTO;
import com.ryu.weather.service.ForecastService;
import com.ryu.weather.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class ScheduledTasks {

    //field
    private ForecastService forecast;
    private LocationService location;

    //cons
    public ScheduledTasks(ForecastService forecast, LocationService location) {
        this.forecast = forecast;
        this.location = location;
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void weatherScheduler() throws IOException {
        try {
            List<LocationDTO> locations = location.getLocationAll();
            for(LocationDTO loc : locations){
                List<ForecastDTO> forecasts = forecast.getForecastFromApi(loc.getFcstCoordinate());
                for (ForecastDTO forc: forecasts) {
                    String time = forc.getFcstTime();
                    if(time.equals("0600")||time.equals("0900")||
                       time.equals("1200")||time.equals("1500")||
                       time.equals("1800")||time.equals("2100")||
                       time.equals("2300")){
                        forecast.insertForecast(forc);
                    }  //END if
                }  //END for
            } //END for
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
