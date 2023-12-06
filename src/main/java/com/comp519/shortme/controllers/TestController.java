package com.comp519.shortme.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("all")
    public String all(HttpServletRequest request) {
        String ad = request.getRemoteAddr();
        System.out.println(ad);
        return "Unprotected Route";
    }

    @GetMapping("test")
    public String test() {

        return "Protected";
    }
}
