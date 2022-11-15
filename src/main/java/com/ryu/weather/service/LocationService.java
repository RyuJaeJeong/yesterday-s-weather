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

    // field
    private LocationRepository repository;


    // cons
    @Autowired
    public LocationService(LocationRepository repository) {
        this.repository = repository;
    }


    // method

    /**
     * 지역정보를 추가
     * @param dto 지역정보 객체
     * fcstCoordinate
     * weatherCoordinate
     * name
     */
    public void insertLocation(LocationDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        LocationEntity entity = modelMapper.map(dto, LocationEntity.class);
        repository.save(entity);
    }

    /**
     * 지역정보를 모두 불러옮
     * @return 지역정보 리스트
     */
    public List<LocationDTO> getLocationAll() {
        ModelMapper modelMapper = new ModelMapper();
        List<LocationEntity> entityList  = repository.findAll();
        List<LocationDTO> dtoList = entityList.stream().map(LocationEntity->modelMapper.map(LocationEntity, LocationDTO.class)).collect(Collectors.toList());
        return dtoList;
    }

}
