module com.example.lab5 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens com.example.lab5 to javafx.fxml;
    exports com.example.lab5;
}