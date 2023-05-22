module com.example.lab009 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.lab009 to javafx.fxml;
    exports com.example.lab009;
}