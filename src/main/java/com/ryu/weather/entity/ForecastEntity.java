package com.ryu.weather.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="t_weather")
public class ForecastEntity {
    @EmbeddedId
    private ForecastId forecastId;

    @Column(columnDefinition = "", nullable = true)
    private String TMP;

    @Column(columnDefinition = "", nullable = true)
    private String UUU;

    @Column(columnDefinition = "", nullable = true)
    private String VVV;

    @Column(columnDefinition = "", nullable = true)
    private String VEC;

    @Column(columnDefinition = "", nullable = true)
    private String WSD;

    @Column(columnDefinition = "", nullable = true)
    private String SKY;

    @Column(columnDefinition = "", nullable = true)
    private String PTY;

    @Column(columnDefinition = "", nullable = true)
    private String POP;

    @Column(columnDefinition = "", nullable = true)
    private String WAV;

    @Column(columnDefinition = "", nullable = true)
    private String PCP;

    @Column(columnDefinition = "", nullable = true)
    private String REH;

    @Column(columnDefinition = "", nullable = true)
    private String SNO;

    @Column(columnDefinition = "", nullable = true)
    private String TMN;

    @Column(columnDefinition = "", nullable = true)
    private String TMX;
}
