package com.ryu.weather.controller;

import com.ryu.weather.dto.ForecastDTO;
import com.ryu.weather.dto.WeatherDTO;
import com.ryu.weather.payload.CommonResponse;
import com.ryu.weather.service.ForecastService;
import com.ryu.weather.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class YesterdayController {

    // field
    private ForecastService forecastService;
    private WeatherService weatherService;

    // cons
    @Autowired
    public YesterdayController(ForecastService forecastService, WeatherService weatherService) {
        this.forecastService = forecastService;
        this.weatherService = weatherService;
    }

    // method
    @GetMapping("/yesterday")
    public ModelAndView goYesterday() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("yesterday");
        return mv;
    }

    /**
     * DB에 저장된 예보 정보를 조회하는 메서드
     * @param when 날짜
     * @param location 지역좌표
     * @return
     */
    @GetMapping(value = "/forecast/{when}/{location}" , produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CommonResponse> getForecast(@PathVariable("when") String when, @PathVariable("location") String location){
        CommonResponse<List<ForecastDTO>> response = new CommonResponse<>();
        try{
            List<ForecastDTO> forecastList = forecastService.getForecast(when, location);
            int size = forecastList.size();
            if(size > 0){
                response.setCode(HttpStatus.OK.value());
                response.setHttpStatus(HttpStatus.OK);
                response.setMessage("예보정보 조회 성공");
                response.setData(forecastList);
                response.setCount(1);
            }else{
                response.setCode(HttpStatus.NOT_FOUND.value());
                response.setHttpStatus(HttpStatus.NOT_FOUND);
                response.setMessage("예보정보 조회에 실패하였습니다.");
                response.setData(forecastList);
                response.setCount(0);
            }
        }catch (Exception e){
            e.printStackTrace();
            response.setCode(HttpStatus.BAD_GATEWAY.value());
            response.setHttpStatus(HttpStatus.BAD_GATEWAY);
            response.setMessage("관리자에게 문의하세요.");
            response.setData(null);
            response.setCount(-1);
        }

        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    /**
     * API에서 기상청 시간자료를 조회
     * @param when 날짜, api에서 어제 까지만 조회 가능
     * @param location 지역 좌표
     * @return
     */
    @GetMapping(value = "/hourlyWeather/{when}/{location}" , produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CommonResponse> getWeather(@PathVariable("when") String when, @PathVariable("location") String location){
        CommonResponse<List<WeatherDTO>> response = new CommonResponse<>();
        try{
            //시간 자료 서비스 최대 9시간 까지 조회 가능
            List<WeatherDTO> weatherList = weatherService.getWeatherFromApi(when, location, "06", "15");
            List<WeatherDTO> weatherList2 = weatherService.getWeatherFromApi(when, location, "18", "23");
            List<WeatherDTO> weatherListFinal = new ArrayList<>();
            for (WeatherDTO dto : weatherList){
                switch (dto.getWeatherTime()){
                    case "06:00":
                    case "12:00":
                    case "15:00":weatherListFinal.add(dto);
                }
            }

            for (WeatherDTO dto : weatherList2){
                switch (dto.getWeatherTime()){
                    case "18:00":
                    case "23:00":weatherListFinal.add(dto);
                }
            }

            int size = weatherListFinal.size();
            if(size > 0){
                response.setCode(HttpStatus.OK.value());
                response.setHttpStatus(HttpStatus.OK);
                response.setMessage("시간자료 조회 성공");
                response.setData(weatherListFinal);
                response.setCount(1);
            }else{
                response.setCode(HttpStatus.NOT_FOUND.value());
                response.setHttpStatus(HttpStatus.NOT_FOUND);
                response.setMessage("시간자료 조회에 실패하였습니다.");
                response.setData(weatherListFinal);
                response.setCount(0);
            }
        }catch (Exception e){
            e.printStackTrace();
            response.setCode(HttpStatus.BAD_GATEWAY.value());
            response.setHttpStatus(HttpStatus.BAD_GATEWAY);
            response.setMessage("관리자에게 문의하세요.");
            response.setData(null);
            response.setCount(-1);
        }

        return new ResponseEntity<>(response, response.getHttpStatus());
    }



}
