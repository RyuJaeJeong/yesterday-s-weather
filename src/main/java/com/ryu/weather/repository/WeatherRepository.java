package com.ryu.weather.repository;

import com.ryu.weather.entity.WeatherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WeatherRepository extends JpaRepository<WeatherEntity, String> {

    @Query("SELECT distinct T1 FROM WeatherEntity T1 LEFT JOIN T1.weatherId.location WHERE T1.weatherId.weatherDate = :WeatherDate AND T1.weatherId.location.locationNo = :locationNo ")
    public List<WeatherEntity> findByWeatherId_Location_WeatherCoordinateAndWeatherIdWeatherDate(@Param("WeatherDate") String WeatherDate, @Param("locationNo") int locationNo);

}
