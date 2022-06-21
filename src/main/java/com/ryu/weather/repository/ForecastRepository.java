package com.ryu.weather.repository;

import com.ryu.weather.entity.ForecastEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForecastRepository extends JpaRepository<ForecastEntity, String> {

}
