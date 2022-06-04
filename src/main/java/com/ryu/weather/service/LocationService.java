package com.ryu.weather.service;

import com.ryu.weather.dto.LocationDTO;
import com.ryu.weather.entity.LocationEntity;
import com.ryu.weather.repository.LocationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationService {

    @Autowired
    LocationRepository repository;

    //entity와 dto클래스를 매핑시켜주는 역활을 함
    ModelMapper modelMapper = new ModelMapper();

    //지역정보를 추가
    public void insertLocation(LocationDTO dto) {
        LocationEntity entity = modelMapper.map(dto, LocationEntity.class);
        System.out.println("=========================================");
        System.out.println(repository.save(entity));
    }

    //위치좌표 리스트를 불러옴.
    public List<LocationDTO> getLocations() {
        List<LocationEntity> entityList  = repository.findAll();
        List<LocationDTO> dtoList = entityList.stream().map(LocationEntity->modelMapper.map(LocationEntity, LocationDTO.class)).collect(Collectors.toList());
        return dtoList;
    }

    //위치좌표를 받아 지역명을 불러옴.
    public LocationDTO getLocation(String coordinate){
        LocationEntity entity = repository.findById(coordinate).get();
        LocationDTO dto = modelMapper.map(entity, LocationDTO.class);
        return dto;
    }



}
