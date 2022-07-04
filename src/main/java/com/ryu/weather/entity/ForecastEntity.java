package com.ryu.weather.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="t_weather")
public class ForecastEntity {
    @EmbeddedId
    private ForecastId forecastId;

    @Column(columnDefinition = "varchar(10)", nullable = true)
    private String TMP;

    @Column(columnDefinition = "varchar(10)", nullable = true)
    private String UUU;

    @Column(columnDefinition = "varchar(10)", nullable = true)
    private String VVV;

    @Column(columnDefinition = "varchar(10)", nullable = true)
    private String VEC;

    @Column(columnDefinition = "varchar(10)", nullable = true)
    private String WSD;

    @Column(columnDefinition = "varchar(10)", nullable = true)
    private String SKY;

    @Column(columnDefinition = "varchar(10)", nullable = true)
    private String PTY;

    @Column(columnDefinition = "varchar(10)", nullable = true)
    private String POP;

    @Column(columnDefinition = "varchar(10)", nullable = true)
    private String WAV;

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
