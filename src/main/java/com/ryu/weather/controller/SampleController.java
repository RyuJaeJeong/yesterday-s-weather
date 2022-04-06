package com.ryu.weather.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SampleController {

    @GetMapping("")
    public String sample() {
        return "tables";
    }


}
