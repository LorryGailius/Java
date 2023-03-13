package com.example.mortgage_calculator;

import java.time.LocalDate;

abstract class Loan {

    public Month[] months;

    public Loan(int months) {
        this.months = new Month[months];
    }

    abstract public void calculateLoan(double amount, double interest, LocalDate startDate, LocalDate endDate);

    public void deffer(LocalDate startDate, int defferalPeriod, double interest) {
        int n = months.length;
        double amount = months[0].getBalance();
        Month[] newMonths = new Month[n + defferalPeriod];

        for (int i = 0; i < n; i++) {
            if (months[i].getDate().isBefore(startDate)) {
                newMonths[i] = months[i];
                amount = months[i].getBalance();
            } else {
                months[i].setDate(months[i].getDate().plusMonths(defferalPeriod));
                newMonths[i + defferalPeriod] = months[i];
            }
        }
        int temp = 0;

        for (int i = 0; i < newMonths.length; i++) {
            if(newMonths[i] == null) {
                newMonths[i] = new Month();
                newMonths[i].setDate(startDate.plusMonths(temp));
                temp++;
                newMonths[i].setTotalPayment(amount * (interest / 100 / 12));
                newMonths[i].setBalance(amount);
            }
        }
        months = newMonths;
    }

    public void printLoan(){
        for (Month month : months){
            System.out.println(month);
        }
    }

}