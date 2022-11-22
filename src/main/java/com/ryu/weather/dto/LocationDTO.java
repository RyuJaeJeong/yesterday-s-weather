package com.ryu.weather.dto;

import lombok.*;

@Data
@RequiredArgsConstructor
public class LocationDTO {

    //field
    private int locationNo;
    private String fcstCoordinate;
    private String weatherCoordinate;
    private String name;
    private String seq;

}
