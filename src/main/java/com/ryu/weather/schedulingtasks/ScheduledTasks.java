package com.ryu.weather.schedulingtasks;

import com.ryu.weather.common.Util;
import com.ryu.weather.dto.ForecastDTO;
import com.ryu.weather.dto.LocationDTO;
import com.ryu.weather.dto.WeatherDTO;
import com.ryu.weather.service.ForecastService;
import com.ryu.weather.service.LocationService;
import com.ryu.weather.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class ScheduledTasks {

    //field
    private ForecastService forecastService;
    private LocationService locationService;
    private WeatherService weatherService;

    //cons
    public ScheduledTasks(ForecastService forecast, LocationService location, WeatherService weather) {
        this.forecastService = forecast;
        this.locationService = location;
        this.weatherService = weather;
    }

    @Scheduled(cron = "0 8 23 * * *")
    public void weatherScheduler() throws IOException {
        try {
            List<LocationDTO> locations = locationService.getLocationAll();
            for(LocationDTO loc : locations){
                // 예보 저장
                List<ForecastDTO> forecasts = forecastService.getForecastFromApi(loc.getFcstCoordinate());
                for (ForecastDTO forc: forecasts) {
                    String time = forc.getFcstTime();
                    if(time.equals("0600")||time.equals("0900")||
                       time.equals("1200")||time.equals("1500")||
                       time.equals("1800")||time.equals("2100")||
                       time.equals("2300")){
                        forc.setLocationNo(loc.getLocationNo());
                        forecastService.insertForecast(forc);
                    }  //END if
                }  //END for

                // 날씨 저장
                WeatherDTO minAndMax = weatherService.getWeatherFromApiDaily(Util.getSomeDayMore(-1), loc.getWeatherCoordinate());

                List<WeatherDTO> weathers = weatherService.getWeatherFromApiHourly(Util.getSomeDayMore(-1), loc.getWeatherCoordinate(), "06", "15");
                List<WeatherDTO> result = new ArrayList<>();
                for (WeatherDTO weather: weathers) {
                    String time = weather.getWeatherTime();
                    if(time.equals("0600")){
                        weather.setTMN(minAndMax.getTMN());
                        result.add(weather);
                    }else if(time.equals("0900")|| time.equals("1200")){
                        result.add(weather);
                    }else if(time.equals("1500")){
                        weather.setTMX(minAndMax.getTMX());
                        result.add(weather);
                    }else{

                    }
                }

                weathers = weatherService.getWeatherFromApiHourly(Util.getSomeDayMore(-1), loc.getWeatherCoordinate(), "16", "23");
                for (WeatherDTO weather: weathers) {
                    String time = weather.getWeatherTime();
                    if(time.equals("1800")||time.equals("2100")|| time.equals("2300")){
                        result.add(weather);
                    }
                }

                for (WeatherDTO weather: result){
                    weather.setLocationNo(loc.getLocationNo());
                    weatherService.insertWeather(weather);
                }
                
            } //END for
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
