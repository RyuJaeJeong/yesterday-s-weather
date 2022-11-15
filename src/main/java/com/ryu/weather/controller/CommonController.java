package com.ryu.weather.controller;

import com.ryu.weather.common.Util;
import com.ryu.weather.dto.LocationDTO;
import com.ryu.weather.payload.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class CommonController {

    @GetMapping("/")
    public ModelAndView goHome() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("home");
        return mv;
    }

    @GetMapping(value = "/yesterdaysDate")
    public ResponseEntity<CommonResponse> getYesterdaySdate(){
        CommonResponse<String> response = new CommonResponse<>();
        try {
            String date = Util.getSomeDayMore(-1);
            if(!date.isEmpty()){
                response.setCode(HttpStatus.OK.value());
                response.setHttpStatus(HttpStatus.OK);
                response.setMessage("어제날짜 조회 성공");
                response.setData(date);
                response.setCount(1);
            }else{
                response.setCode(HttpStatus.NOT_FOUND.value());
                response.setHttpStatus(HttpStatus.NOT_FOUND);
                response.setMessage("어제날짜 조회에 실패하였습니다.");
                response.setData("");
                response.setCount(0);
            }
        }catch (Exception e){
            response.setCode(HttpStatus.BAD_GATEWAY.value());
            response.setHttpStatus(HttpStatus.BAD_GATEWAY);
            response.setMessage("관리자에게 문의하세요.");
            response.setData("");
            response.setCount(-1);
        }
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

}
