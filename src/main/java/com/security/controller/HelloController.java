package com.security.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping
    public String hi(HttpServletRequest request){
        return new String("Hi Naveen! welcome."+request.getSession().getId());
    }

}
