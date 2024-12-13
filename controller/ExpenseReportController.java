package com.paymentauth.controller;

import com.paymentauth.entity.ExpenseReport;
import com.paymentauth.service.ExpenseReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/expense-reports")
public class ExpenseReportController {

    private final ExpenseReportService expenseReportService;

    public ExpenseReportController(ExpenseReportService expenseReportService) {
        this.expenseReportService = expenseReportService;
    }

    @GetMapping("/save")
    public ResponseEntity<String> saveUser(){
        return ResponseEntity.ok("success");
    }

    @PostMapping("/create_report")
    public ResponseEntity<ExpenseReport> createExpenseReport(@RequestBody ExpenseReport expenseReport, @AuthenticationPrincipal User principal) {
        String username = principal.getUsername();
        return ResponseEntity.ok(expenseReportService.createExpenseReport(expenseReport, username));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> approveExpenseReport(@PathVariable Long id, @AuthenticationPrincipal User principal) throws IOException {
        String managerUsername = principal.getUsername();
        expenseReportService.approveExpenseReport(id,managerUsername);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> rejectExpenseReport(@PathVariable Long id, @AuthenticationPrincipal User principal) {
        String managerUsername = principal.getUsername();
        expenseReportService.rejectExpenseReport(id, managerUsername);
        return ResponseEntity.ok().build();
    }
}
