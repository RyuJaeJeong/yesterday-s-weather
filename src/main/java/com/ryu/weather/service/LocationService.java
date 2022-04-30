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



    public List<LocationDTO> getLocations() {
        ModelMapper modelMapper = new ModelMapper();
        List<LocationEntity> entityList  = repository.findAll();
        List<LocationDTO> dtoList = entityList.stream().map(LocationEntity->modelMapper.map(LocationEntity, LocationDTO.class)).collect(Collectors.toList());
        return dtoList;
    }


}
