package Controllers;

import com.example.mortgage_calculator.AnnuityLoan;
import com.example.mortgage_calculator.LinearLoan;
import com.example.mortgage_calculator.Month;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class WindowController implements Initializable {

    AnnuityLoan annuityLoan;
    LinearLoan linearLoan;

    ObservableList<Month> annuityData = FXCollections.observableArrayList();

    ObservableList<Month> linearData = FXCollections.observableArrayList();

    FileController fileController = new FileController();

    long months;

    @FXML
    private TableColumn<Month, LocalDate> date_col;

    @FXML
    private LineChart chart;

    @FXML
    private XYChart.Series annuity;

    @FXML
    private XYChart.Series linear;

    @FXML
    private TableColumn<Month, Double> total_col;

    @FXML
    private TableColumn<Month, Double> principal_col;

    @FXML
    private TableColumn<Month, Double> interest_col;

    @FXML
    private TableColumn<Month, Double> balance_col;

    @FXML
    private TableView<Month> table;

    @FXML
    private ChoiceBox<String> loan_type;

    @FXML
    private TextField amount_field;

    @FXML
    private TextField interest_field;

    @FXML
    private DatePicker start_date;

    @FXML
    private DatePicker end_date;

    @FXML
    private DatePicker start_date1;

    @FXML
    private DatePicker end_date1;

    @FXML
    private DatePicker defferal_date;

    @FXML
    private TextField defferal_period;

    @FXML
    private TextField defferal_interest;

    @FXML
    public void onCalculateButtonClick() {

        // Check if date range is valid
        if(start_date.getValue() == null || end_date.getValue() == null) {
            System.out.println("Invalid input!");
            return;
        }

        // Check if input is valid
        try {
            Double.parseDouble(amount_field.getText());
            Double.parseDouble(interest_field.getText());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
            return;
        }

        // Check if month difference is valid
        months = start_date.getValue().getDayOfMonth() == end_date.getValue().getDayOfMonth() ? ChronoUnit.MONTHS.between(start_date.getValue(), end_date.getValue()) : 0;
        if(months == 0) {
            System.out.println("Invalid date range!");
            return;
        }

        // Calculate loan
        annuityLoan = new AnnuityLoan((int) months);
        annuityLoan.calculateLoan(Double.parseDouble(amount_field.getText()), Double.parseDouble(interest_field.getText()), start_date.getValue(), end_date.getValue());
        annuityData = FXCollections.observableArrayList(annuityLoan.months);
        linearLoan = new LinearLoan((int) months);
        linearLoan.calculateLoan(Double.parseDouble(amount_field.getText()), Double.parseDouble(interest_field.getText()), start_date.getValue(), end_date.getValue());
        linearData = FXCollections.observableArrayList(linearLoan.months);

        // Update table
        changeLoanType(loan_type.getValue());
    }

    @FXML
    public void onGraphButtonClick() {
        table.setVisible(!table.isVisible());
        chart.setVisible(!chart.isVisible());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Make chart invisible
        chart.setVisible(false);
        // Initialize selection box
        loan_type.getItems().addAll("Linear", "Annuity");
        loan_type.setValue("Linear");
        loan_type.valueProperty().addListener((observable, oldValue, newValue) -> changeLoanType(newValue));
        // Initialize table
        date_col.setCellValueFactory(new PropertyValueFactory<>("date"));
        total_col.setCellValueFactory(new PropertyValueFactory<>("totalPayment"));
        principal_col.setCellValueFactory(new PropertyValueFactory<>("principal"));
        interest_col.setCellValueFactory(new PropertyValueFactory<>("interest"));
        balance_col.setCellValueFactory(new PropertyValueFactory<>("balance"));
        date_col.setReorderable(false);
        total_col.setReorderable(false);
        principal_col.setReorderable(false);
        interest_col.setReorderable(false);
        balance_col.setReorderable(false);
    }

    public void changeLoanType(String type) {

        if(linearLoan == null || annuityLoan == null) {
            return;
        }

        if(type.equals("Linear")) {
            table.setItems(linearData);
            fileController.setMonths(linearLoan.months);
        } else {
            table.setItems(annuityData);
            fileController.setMonths(annuityLoan.months);
        }

        clearChart();

        for (Month month : annuityData) {
            annuity.getData().add(new XYChart.Data(month.getDate().toString(), month.getTotalPayment()));
        }

        for (Month month : linearData) {
            linear.getData().add(new XYChart.Data(month.getDate().toString(), month.getTotalPayment()));
        }
        chart.getXAxis().setTickLabelsVisible(true);
        chart.getData().addAll(annuity, linear);
    }

    public void changeLoanType(ObservableList<Month> filteredDataL, ObservableList<Month> filteredDataA, String type){
        if(type.equals("Linear")) {
            table.setItems(filteredDataL);
        } else {
            table.setItems(filteredDataA);
        }

        clearChart();

        for (Month month : filteredDataA) {
            annuity.getData().add(new XYChart.Data(month.getDate().toString(), month.getTotalPayment()));
        }

        for (Month month : filteredDataL) {
            linear.getData().add(new XYChart.Data(month.getDate().toString(), month.getTotalPayment()));
        }
        chart.getXAxis().setTickLabelsVisible(true);
        chart.getData().addAll(annuity, linear);
    }

    public ObservableList<Month> getDataType() {
        if(loan_type.getValue().equals("Linear")) {
            return linearData;
        } else {
            return annuityData;
        }
    }

    public void clearChart() {
        chart.getData().clear();
        annuity = new XYChart.Series();
        linear = new XYChart.Series();
        annuity.setName("Annuity");
        linear.setName("Linear");
    }

    public void filterData() {
        if(start_date1.getValue() == null || end_date1.getValue() == null) {
            System.out.println("Invalid filter input!");
            return;
        }

        ObservableList<Month> filteredDataL = FXCollections.observableArrayList();
        ObservableList<Month> filteredDataA = FXCollections.observableArrayList();

        for (Month month : linearData) {
            if(month.getDate().isAfter(start_date1.getValue().minusDays(1)) && month.getDate().isBefore(end_date1.getValue().plusDays(1))) {
                filteredDataL.add(month);
            }
        }

        for (Month month : annuityData) {
            if(month.getDate().isAfter(start_date1.getValue().minusDays(1)) && month.getDate().isBefore(end_date1.getValue().plusDays(1))) {
                filteredDataA.add(month);
            }
        }

        changeLoanType(filteredDataL,filteredDataA,loan_type.getValue());
    }

    public void onDefferalButtonClicked(){
        if(defferal_date.getValue() == null || defferal_period.getText().isEmpty() || defferal_interest.getText().isEmpty()) {
            System.out.println("Invalid defferal input!");
            return;
        }

        if(linearLoan == null || annuityLoan == null) {
            System.out.println("No data to deffer!");
            return;
        }

        annuityData.clear();
        linearData.clear();

        annuityLoan.deffer(defferal_date.getValue(), Integer.parseInt(defferal_period.getText()), Double.parseDouble(defferal_interest.getText()));
        annuityData = FXCollections.observableArrayList(annuityLoan.months);
        linearLoan.deffer(defferal_date.getValue(), Integer.parseInt(defferal_period.getText()), Double.parseDouble(defferal_interest.getText()));
        linearData = FXCollections.observableArrayList(linearLoan.months);
        changeLoanType(loan_type.getValue());
    }

    public void onReportButtonClicked(){
        if(linearLoan == null || annuityLoan == null) {
            System.out.println("No data to write!");
            return;
        }
        changeLoanType(loan_type.getValue());
        fileController.write_report();
    }


}
