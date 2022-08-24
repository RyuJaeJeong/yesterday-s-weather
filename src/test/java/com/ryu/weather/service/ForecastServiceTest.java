package com.ryu.weather.service;


import com.ryu.weather.dto.ForecastDTO;
import com.ryu.weather.dto.LocationDTO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class ForecastServiceTest {

    @Autowired
    ForecastService forecast;

    @Autowired
    LocationService location;

    ModelMapper modelMapper = new ModelMapper();

    @Test
    void insertForecast() throws IOException {
        List<LocationDTO> locations = location.getLocationAll();
        for(LocationDTO loc:locations){
            List<ForecastDTO> forecasts = forecast.getForecastFromApi(loc.getCoordinate());
            for (ForecastDTO forc: forecasts) {
                String time = forc.getFcstTime();
                if(time.equals("0600")||time.equals("1200")||
                   time.equals("1500")||time.equals("1800")||
                   time.equals("2300")){
                    forecast.insertForecast(forc);
                }
            }
        }
    }

    @Test
    void getForecastFromApi() throws IOException {
        List<ForecastDTO> list = forecast.getForecastFromApi("088089");
        for (ForecastDTO dto:list){
            System.out.println(dto.toString());
        }
    }

    @Test
    void getForecast() throws IOException {
        System.out.println(System.currentTimeMillis());
        List<ForecastDTO> list = forecast.getForecast("20220824", "089091");
        for (ForecastDTO dto:list){
            System.out.println(dto.toString());
        }
        System.out.println(System.currentTimeMillis());
    }

}