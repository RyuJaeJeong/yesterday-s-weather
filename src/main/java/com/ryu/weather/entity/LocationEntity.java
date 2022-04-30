package com.ryu.weather.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_location")
public class LocationEntity {
    @Id
    private String coordinate;

    @Column(columnDefinition = "varchar(20)", nullable = true)
    private String name;

    public LocationEntity() {

    }

    public LocationEntity(String coordinate, String name) {
        this.coordinate = coordinate;
        this.name = name;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
