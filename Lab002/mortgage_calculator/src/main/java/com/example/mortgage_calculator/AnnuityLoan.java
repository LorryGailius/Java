package com.example.mortgage_calculator;

import java.time.LocalDate;

public class AnnuityLoan extends Loan {

    public AnnuityLoan(int months) {
        super(months);
    }

    @Override
    public void calculateLoan(double amount, double interest, LocalDate startDate, LocalDate endDate) {
        int n = months.length;
        double interestRate = interest / 100 / 12;
        double annuity = amount * interestRate / (1 - 1 / Math.pow(1 + interestRate, n));

        for (int i = 0; i < n; i++) {
            months[i] = new Month();
            months[i].setDate(startDate.plusMonths(i));
            months[i].setTotalPayment(annuity);
            months[i].setInterest(interestRate * amount * (1 - (double)i / n));
            months[i].setPrincipal(annuity - months[i].getInterest());
            months[i].setBalance(amount - (amount / n) * (i + 1));
        }
    }
}
