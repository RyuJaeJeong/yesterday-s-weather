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
    private String TA;              //기온 ta
    private String WD;              //풍향 wd
    private String WS;              //풍속 ws
    private String dmstMtphNo;      //현상 번호
    private String RN;              //강수량
    private String HM;              //습도
    private String DSNW;            //적설
    private String minTa;           //최저기온
    private String maxTa;           //최고기온

}
