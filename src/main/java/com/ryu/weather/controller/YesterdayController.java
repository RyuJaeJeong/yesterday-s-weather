package com.ryu.weather.controller;

import com.ryu.weather.dto.ForecastDTO;
import com.ryu.weather.dto.WeatherDTO;
import com.ryu.weather.payload.CommonResponse;
import com.ryu.weather.service.ForecastService;
import com.ryu.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class YesterdayController {

    // field
    private final ForecastService forecastService;
    private final WeatherService weatherService;

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
     * @param where 지역좌표
     * @return
     */
    @GetMapping(value = "/forecast/{when}/{where}" , produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CommonResponse> getForecast(@PathVariable("when") String when, @PathVariable("where") int where){
        CommonResponse<List<ForecastDTO>> response = new CommonResponse<>();
        try{
            List<ForecastDTO> forecastList = forecastService.getForecast(when, where);
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

    @GetMapping(value = "/weather/{when}/{where}" , produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CommonResponse> getWeather(@PathVariable("when") String when, @PathVariable("where") int where){
        CommonResponse<List<WeatherDTO>> response = new CommonResponse<>();
        try{
            List<WeatherDTO> weatherList = weatherService.getWeather(when, where);
            int size = weatherList.size();
            if(size > 0){
                response.setCode(HttpStatus.OK.value());
                response.setHttpStatus(HttpStatus.OK);
                response.setMessage("시간자료정보 조회 성공");
                response.setData(weatherList);
                response.setCount(1);
            }else{
                response.setCode(HttpStatus.NOT_FOUND.value());
                response.setHttpStatus(HttpStatus.NOT_FOUND);
                response.setMessage("시간자료정보 조회에 실패하였습니다.");
                response.setData(weatherList);
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
