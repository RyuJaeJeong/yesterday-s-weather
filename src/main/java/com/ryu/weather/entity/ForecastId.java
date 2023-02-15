package com.ryu.weather.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@Data
public class ForecastID implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="location_no")
    private LocationEntity location;

    @Column(columnDefinition = "varchar(8)", name="fcstDate")
    private String fcstDate;

    @Column(columnDefinition = "varchar(4)", name="fcstTime")
    private String fcstTime;

}
