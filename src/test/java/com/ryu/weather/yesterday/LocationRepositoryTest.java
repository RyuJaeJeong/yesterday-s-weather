package com.ryu.weather.yesterday;

import com.ryu.weather.dto.LocationDTO;
import com.ryu.weather.entity.LocationEntity;
import com.ryu.weather.repository.LocationRepository;
import com.ryu.weather.service.LocationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class LocationRepositoryTest {

    @Autowired
    LocationRepository location;

    @Autowired
    LocationService service;


    //hibernate 의 경우, save실행 전, select문을 돌려 key값이 이미 존재하면
    //update, 존재하지 않으면 insert
    @Test
    public void insertTest() {
        LocationEntity loc = new LocationEntity();
        loc.setName("대구");
        loc.setCoordinate("089091");
        location.save(loc);
        loc.setName("");
        loc.setCoordinate("");
        loc.setName("부산");
        loc.setCoordinate("098076");
        location.save(loc);
    }

    @Test
    public void selectTest() {
        List<LocationDTO> list = service.getLocations();
        for (int i=0; i<list.size(); i++) {
            System.out.println(list.get(i).getName());
            System.out.println(list.get(i).getCoordinate());
        }
    }

}