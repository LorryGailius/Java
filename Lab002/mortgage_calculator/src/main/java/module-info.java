module com.example.mortgage_calculator {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.example.mortgage_calculator to javafx.fxml;
    exports com.example.mortgage_calculator;
    exports Controllers;
    opens Controllers to javafx.fxml;
}