package com.ryu.weather.service;

import com.ryu.weather.dto.LocationDTO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;



@RunWith(SpringRunner.class)
@SpringBootTest
class LocationServiceTest {

    @Autowired
    LocationService service;

    @Test
    void insertLocation(){
        LocationDTO dto = new LocationDTO("144123", "독도");
        service.insertLocation(dto);
    }

    @Test
    void getLocations() {
        List<LocationDTO> list = service.getLocations();
        for (int i=0; i<list.size(); i++) {
            System.out.print(list.get(i).getName() + " : ");
            System.out.println(list.get(i).getCoordinate());
        }
    }

    @Test
    void getLocation() {
        System.out.println(service.getLocation("052038").toString());
    }


}

