package com.comp519.shortme.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("all")
    public String all() {
        return "Unprotected Route";
    }

    @GetMapping("test")
    public String test() {
        return "Protected";
    }
}
