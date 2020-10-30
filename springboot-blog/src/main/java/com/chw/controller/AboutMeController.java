package com.chw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Create by CHW on 2020/9/24 14:54
 */
@Controller
public class AboutMeController {
    @GetMapping("/about")
    public String about(){
        return "about";
    }
}
