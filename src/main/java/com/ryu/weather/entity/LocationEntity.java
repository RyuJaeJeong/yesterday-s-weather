package com.ryu.weather.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name="t_location")
public class LocationEntity {

    @Id
    private String fcstCoordinate;

    @Column(columnDefinition = "varchar(3)", nullable = true)
    private String weatherCoordinate;

    @Column(columnDefinition = "varchar(20)", nullable = true)
    private String name;

    public LocationEntity(String name, String fcstCoordinate) {
        this.name = name;
        this.fcstCoordinate = fcstCoordinate;
    }

    public LocationEntity() {

    }
}
