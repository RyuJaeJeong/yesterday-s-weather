package com.ryu.weather.dto;


import lombok.*;

//단기예보
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForecastDTO {

    //field
    private String coordinate;
    private String locationName;
    private String fcstDate;
    private String fcstTime;
    private String TMP;         //1시간 기온 도
    private String VEC;         //풍향  deg
    private String WSD;         //풍속  m/s
    private String SKY;         //하늘상태 코드값
    private String PTY;         //강수형태 코드값
    private String POP;         //강수확률 %
    private String PCP;         //1시간 강수량
    private String REH;         //습도 %
    private String SNO;         //1시간 신적설
    private String TMN;         //최저기온 도
    private String TMX;         //최고기온 도


}
