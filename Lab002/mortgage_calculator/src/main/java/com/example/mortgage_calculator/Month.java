package com.example.mortgage_calculator;

import java.text.DecimalFormat;
import java.time.LocalDate;

public class Month {

    public LocalDate date;

    private final DecimalFormat df = new DecimalFormat("#.##");
    public double totalPayment;

    public double principal;

    public double interest;

    public double balance;

    public Month() {
        this.setDate(null);
        this.setTotalPayment(0);
        this.setPrincipal(0);
        this.setInterest(0);
        this.setBalance(0);
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    public double getPrincipal() {
        return principal;
    }

    public double getInterest() {
        return interest;
    }

    public double getBalance() {
        return balance;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = Double.parseDouble(df.format(totalPayment));
    }

    public void setPrincipal(double principal) {
        this.principal = Double.parseDouble(df.format(principal));
    }

    public void setInterest(double interest) {
        this.interest = Double.parseDouble(df.format(interest));
    }

    public void setBalance(double balance) {
        this.balance = Double.parseDouble(df.format(balance));
    }

    @Override
    public String toString() {
        return date + " " + totalPayment + " " + principal + " " + interest + " " + balance;
    }
}
