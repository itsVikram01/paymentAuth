package com.paymentauth.service;

import com.paymentauth.entity.ExpenseReport;
import com.paymentauth.enums.PaymentMode;
import com.paymentauth.enums.Role;
import com.paymentauth.enums.Status;
import com.paymentauth.exception.ResourceNotFoundException;
import com.paymentauth.repository.ExpenseReportRepository;
import com.paymentauth.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileWriter;
import java.io.IOException;


@RestControllerAdvice
@Service
public class ExpenseReportService {

    private final ExpenseReportRepository expenseReportRepository;
    private final UserRepository userRepository;
    public ExpenseReportService(ExpenseReportRepository expenseReportRepository, UserRepository userRepository) {
        this.expenseReportRepository = expenseReportRepository;
        this.userRepository = userRepository;
    }




    // Methods for creating, approving, rejecting, and processing payments
    public ExpenseReport createExpenseReport(ExpenseReport expenseReport, String username) {
        com.paymentauth.entity.User user = userRepository.findByUsername(username);
        expenseReport.setUser(user);
        expenseReport.setStatus(Status.PENDING);
        return expenseReportRepository.save(expenseReport);
    }

    public void approveExpenseReport(Long reportId, String managerUsername) throws IOException {
        ExpenseReport report = expenseReportRepository.findById(reportId).orElseThrow(() -> new ResourceNotFoundException("Expense report not found"));

        com.paymentauth.entity.User approver = userRepository.findByUsername(managerUsername);
        if (approver.getRoles() != Role.MANAGER) {
            throw new AccessDeniedException("Only managers can approve expense reports");
        }

        report.setStatus(Status.APPROVED);
        report.setApprover(approver);
        expenseReportRepository.save(report);

        processPayment(report);
    }

    public void rejectExpenseReport(Long reportId, String managerUsername) {
        ExpenseReport report = expenseReportRepository.findById(reportId).orElseThrow(() -> new ResourceNotFoundException("Expense report not found"));

        com.paymentauth.entity.User approver = userRepository.findByUsername(managerUsername);
        if (approver.getRoles() != Role.MANAGER) {
            throw new AccessDeniedException("Only managers can reject expense reports");
        }

        report.setStatus(Status.REJECTED);
        report.setApprover(approver);
        expenseReportRepository.save(report);
    }

    public void processPayment(ExpenseReport expenseReport) throws IOException {
        try {
            PaymentMode paymentMode = expenseReport.getPaymentMode();
            if (paymentMode == PaymentMode.BANK_TRANSFER) {
                expenseReport.setStatus(Status.PAID);
            } else if (paymentMode == PaymentMode.CREDIT_CARD) {
                expenseReport.setStatus(Status.PAID);
            } else if (paymentMode == PaymentMode.DEBIT_CARD) {
                expenseReport.setStatus(Status.PAID);
            }
        } catch (Exception e) {
            expenseReport.setStatus(Status.REJECTED);
            throw new IOException(e);
        }

        expenseReportRepository.save(expenseReport);

        generateReport(expenseReport);
    }


    public void generateReport(ExpenseReport expenseReport) throws IOException {
        String csvContent = generateCsv(expenseReport); // Implement CSV generation method
//        String htmlContent = generateHtml(expenseReport); // Implement HTML generation method

        String filename = "expense_report_" + expenseReport.getId() + ".csv";
        FileWriter writer = new FileWriter(filename);
        writer.write(csvContent);
        writer.close();

        System.out.println("Generated CSV report: " + filename);
//        System.out.println("Generated HTML report: " + filename);
    }

    private String generateCsv(ExpenseReport expenseReport) {
        StringBuilder csv = new StringBuilder();
        csv.append("Description,Amount,Payment Mode,Status,Submitted By\n")
                .append(expenseReport.getDescription() + "," +
                        expenseReport.getAmount() + "," +
                        expenseReport.getPaymentMode() + "," +
                        expenseReport.getStatus() + "," +
                        expenseReport.getUser().getUsername() + "\n");

        return csv.toString();
    }

//    private String generateHtml(ExpenseReport expenseReport) {
//        StringBuilder html = new StringBuilder();
//        html.append("<!DOCTYPE html>\n")
//                .append("<html>\n")
//                .append("<head>\n")
//                .append("<title>Expense Report</title>\n")
//                .append("<style>\n")
//                .append("/* Add your CSS styles here */\n")
//                .append("</style>\n")
//                .append("</head>\n")
//                .append("<body>\n")
//                .append("<h1>Expense Report</h1>\n")
//                .append("<p><strong>Description:</strong> " + expenseReport.getDescription() + "</p>\n")
//                .append("<p><strong>Amount:</strong> $" + expenseReport.getAmount() + "</p>\n")
//                .append("<p><strong>Payment Mode:</strong> " + expenseReport.getPaymentMode() + "</p>\n")
//                .append("<p><strong>Status:</strong> " + expenseReport.getStatus() + "</p>\n")
//                .append("<p><strong>Submitted By:</strong> " + expenseReport.getUser().getUsername() + "</p>\n");
//
//
//        html.append("</body>\n")
//                .append("</html>");
//
//        return html.toString();
//    }


}
