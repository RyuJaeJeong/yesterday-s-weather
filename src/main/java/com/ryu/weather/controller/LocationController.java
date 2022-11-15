package com.ryu.weather.controller;

import com.ryu.weather.dto.LocationDTO;
import com.ryu.weather.payload.CommonResponse;
import com.ryu.weather.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class LocationController {

    //field
    private LocationService locationService;

    //cons
    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    //method
    @GetMapping(value = "/locations", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<CommonResponse> getLocationAll(){
        CommonResponse<List<LocationDTO>> response = new CommonResponse<>();
        try{
            List<LocationDTO> locations = locationService.getLocationAll();
            if(locations.size() > 0){
                response.setCode(HttpStatus.OK.value());
                response.setHttpStatus(HttpStatus.OK);
                response.setMessage("지역정보 조회 성공");
                response.setData(locations);
                response.setCount(1);
            }else{
                response.setCode(HttpStatus.NOT_FOUND.value());
                response.setHttpStatus(HttpStatus.NOT_FOUND);
                response.setMessage("지역정보 조회에 실패하였습니다.");
                response.setData(locations);
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
