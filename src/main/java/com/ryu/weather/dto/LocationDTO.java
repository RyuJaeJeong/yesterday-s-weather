package com.ryu.weather.dto;

public class LocationDTO {

    //field
    private String coordinate;
    private String name;

    //cons
    public LocationDTO() {

    }

    public LocationDTO(String coordinate, String name) {
        this.coordinate = coordinate;
        this.name = name;
    }

    //getter&setter
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
