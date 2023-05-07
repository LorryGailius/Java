module com.example.lab006 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.lab006 to javafx.fxml;
    exports com.example.lab006;
}