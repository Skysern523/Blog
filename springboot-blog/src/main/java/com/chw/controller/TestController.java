package com.chw.controller;


import com.chw.handler.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author CHW
 * @version 1.0
 * @date 2020/9/21 17:19
 */
@Controller
public class TestController {
    //@GetMapping("/")
    //public String test(@PathVariable int id, @PathVariable String name){
//        String blog = null;
//        if(blog == null){
//            throw  new NotFoundException("blog不存在");
//        }
//        System.out.println("--------index------");
//        return "AA";
//    }
}
