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
    private int locationNo;

    @Column(columnDefinition = "varchar(6)", nullable = false)
    private String fcstCoordinate;

    @Column(columnDefinition = "varchar(3)", nullable = false)
    private String weatherCoordinate;

    @Column(columnDefinition = "varchar(20)")
    private String name;

    @Column(columnDefinition = "varchar(3)")
    private String seq;
    
}
