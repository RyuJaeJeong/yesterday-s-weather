package com.ryu.weather.repository;

import com.ryu.weather.entity.ForecastEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ForecastRepository extends JpaRepository<ForecastEntity, String> {

    @Query("SELECT DISTINCT T1 FROM ForecastEntity T1 JOIN FETCH T1.forecastId.location WHERE T1.forecastId.fcstDate = :FcstDate AND T1.forecastId.location.locationNo = :locationNo")
    public List<ForecastEntity> findByForecastId_Location_CoordinateAndForecastIdFcstDate(@Param("FcstDate")String FcstDate, @Param("locationNo")int locationNo);

}
