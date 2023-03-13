package Controllers;

import com.example.mortgage_calculator.Month;

import java.io.FileWriter;
import java.io.PrintWriter;

public class FileController {

    private PrintWriter printer;
    private FileWriter writer;

    public Month[] months;

    public FileController(){
    }

    public void setMonths(Month[] months) {
        this.months = months;
    }

    public void write_report(){
        String filePath = "report.csv";
        try {
            writer = new FileWriter(filePath, false);
            printer = new PrintWriter(writer);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        if(months == null){
            System.out.println("Error: No data to write");
            return;
        }
        printer.println("Date, " + "Total Payment, " + "Principal, " + "Interest, " + "Balance");

        for (Month month : months) {
            printer.println(month.getDate() + ", " + month.getTotalPayment() + ", " + month.getPrincipal() + ", " + month.getInterest() + ", " + month.getBalance());
        }

        printer.close();
    }
}
