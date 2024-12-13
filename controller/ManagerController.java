package com.paymentauth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ManagerController {

    @GetMapping("/manager/dashboard")
    public String managerDashboard() {
        return "manager/dashboard";
    }
}
