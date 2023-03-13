package com.example.mortgage_calculator;

import java.time.LocalDate;

public class LinearLoan extends Loan {

    public LinearLoan(int months) {
        super(months);
    }

    @Override
    public void calculateLoan(double amount, double interest, LocalDate startDate, LocalDate endDate) {
        int n = months.length;
        double interestRate = interest / 100 / 12;

        for (int i = 0; i < n; i++) {
            months[i] = new Month();
            months[i].setDate(startDate.plusMonths(i));
            months[i].setTotalPayment((amount / n) + interestRate * (amount - (amount * (i) / n)));
            months[i].setPrincipal(amount / n);
            months[i].setInterest(months[i].getTotalPayment() - months[i].getPrincipal());
            months[i].setBalance(amount - (amount / n) * (i + 1));
        }
    }
}
