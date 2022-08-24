package com.ryu.weather.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@Data
public class ForecastId implements Serializable {

    @ManyToOne
    @JoinColumn(name="coordinate")
    private LocationEntity location;

    @Column(columnDefinition = "varchar(8)", name="fcstDate")
    private String fcstDate;

    @Column(columnDefinition = "varchar(4)", name="fcstTime")
    private String fcstTime;

}
