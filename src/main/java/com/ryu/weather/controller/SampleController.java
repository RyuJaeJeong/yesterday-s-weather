package com.ryu.weather.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class SampleController {

    @GetMapping("/")
    public ModelAndView sample() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("home");
        return mv;
    }

}
