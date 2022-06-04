package com.ryu.weather.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO {

    //field
    private String coordinate;
    private String name;

}
