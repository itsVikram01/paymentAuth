package com.paymentauth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/user/dashboard")
    public String userDashboard() {
        return "user/dashboard";
    }
}