package com.ryu.weather.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name="t_weather")
public class WeatherEntity {

    @EmbeddedId
    private WeatherID weatherId;

    @Column(columnDefinition = "varchar(10)", nullable = true)
    private String TMP;

    @Column(columnDefinition = "varchar(10)", nullable = true)
    private String VEC;

    @Column(columnDefinition = "varchar(10)", nullable = true)
    private String WSD;

    @Column(columnDefinition = "varchar(10)", nullable = true)
    private String dmstMtphNo;

    @Column(columnDefinition = "varchar(10)", nullable = true)
    private String PCP;
    
    @Column(columnDefinition = "varchar(10)", nullable = true)
    private String REH;

    @Column(columnDefinition = "varchar(10)", nullable = true)
    private String SNO;

    @Column(columnDefinition = "varchar(10)", nullable = true)
    private String TMN;

    @Column(columnDefinition = "varchar(10)", nullable = true)
    private String TMX;
}
