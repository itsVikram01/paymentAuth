package com.paymentauth.repository;

import com.paymentauth.entity.ExpenseReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseReportRepository extends JpaRepository<ExpenseReport, Long> {
}
