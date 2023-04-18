module com.example.lab003 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.lab004 to javafx.fxml;
    exports com.example.lab004;
}