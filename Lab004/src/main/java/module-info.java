module com.example.lab003 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.lab003 to javafx.fxml;
    exports com.example.lab003;
}