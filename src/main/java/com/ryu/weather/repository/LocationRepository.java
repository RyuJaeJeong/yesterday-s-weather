package com.ryu.weather.repository;

import com.ryu.weather.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LocationRepository extends JpaRepository<LocationEntity, String> {

    @Query("SELECT new LocationEntity(T1.name, T1.fcstCoordinate) FROM LocationEntity T1 ")
    public List<LocationEntity> findFcstCoordinate();
}
