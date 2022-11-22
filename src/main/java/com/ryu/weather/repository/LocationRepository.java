package com.ryu.weather.repository;

import com.ryu.weather.entity.LocationEntity;
import com.ryu.weather.entity.WeatherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LocationRepository extends JpaRepository<LocationEntity, String> {

    public List<LocationEntity> findAllByOrderBySeqAsc();
    public List<LocationEntity> findAllByOrderBySeqDesc();

}
