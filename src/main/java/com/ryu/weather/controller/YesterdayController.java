package com.ryu.weather.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Slf4j
public class YesterdayController {

    @GetMapping("/yesterday")
    public ModelAndView goYesterday() {
        log.info("yesterday");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("yesterday");
        return mv;
    }

}
