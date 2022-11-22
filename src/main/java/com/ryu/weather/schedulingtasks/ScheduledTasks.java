package com.ryu.weather.schedulingtasks;

import com.ryu.weather.dto.ForecastDTO;
import com.ryu.weather.dto.LocationDTO;
import com.ryu.weather.service.ForecastService;
import com.ryu.weather.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class ScheduledTasks {

    @Autowired
    ForecastService forecast;
    @Autowired
    LocationService location;


    //오전 6시 지정된 위치의 06시, 12시, 15시, 18시, 23시 일기예보 데이터를 DB로 저장하는 스케쥴러
    @Scheduled(cron = "0 0 6 * * *")
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
            log.warn("scheduler error ");
            log.info(String.valueOf(e));
        }
    }
}
