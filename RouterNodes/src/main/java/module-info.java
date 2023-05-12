module com.example.routernodes {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;


    opens com.example.routernodes to javafx.fxml;
    exports com.example.routernodes;
}