package com.ryu.weather.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class WeatherDTO {

    //field
    private String coordinate;      //지역좌표
    private String locationName;    //stnNm
    private String weatherDate;     //tm[0]
    private String weatherTime;     //tm[1]
    private String TMP;
    private String VEC;
    private String WSD;
    private String dmstMtphNo;      //현상 번호
    private String PCP;
    private String REH;
    private String SNO;
    private String TMN;
    private String TMX;

}
