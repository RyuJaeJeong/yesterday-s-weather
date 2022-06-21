package com.ryu.weather.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class ForecastId implements Serializable {

    @Column(columnDefinition = "varchar(6)", name="coordiname")
    private String coordinate;

    @Column(columnDefinition = "varchar(8)", name="fcstDate")
    private String fcstDate;

    @Column(columnDefinition = "varchar(8)", name="fcstTime")
    private String fcstTime;

}
